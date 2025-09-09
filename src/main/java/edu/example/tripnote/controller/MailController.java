package edu.example.tripnote.controller;

import edu.example.tripnote.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    // 1) 인증코드 전송
    @PostMapping("/send-cert")
    @ResponseBody
    public ResponseEntity<?> sendCert(@RequestParam("email") String email, HttpSession session) {
        // 6자리 숫자코드
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
        // 세션 키: 이메일별로 구분
        String key = "EMAIL_CERT::" + email;

        // 세션에 코드/생성시각 저장 (5분 유효)
        Map<String, Object> payload = new HashMap<>();
        payload.put("code", code);
        payload.put("issuedAt", Instant.now());
        session.setAttribute(key, payload);

        // 메일 전송
        String subject = "[TripNote] 이메일 인증코드";
        String body = "인증코드: " + code + "\n(유효시간: 5분)";
        mailService.sendText(email, subject, body); // 메일전송 text

        return ResponseEntity.ok().body("{\"ok\":true}");
    }

    // 2) 인증코드 검증
    @PostMapping("/verify-cert")
    @ResponseBody
    public ResponseEntity<?> verifyCert(@RequestParam("email") String email,
                                        @RequestParam("code") String code,
                                        HttpSession session) {
        String key = "EMAIL_CERT::" + email;
        Object obj = session.getAttribute(key);
        if (!(obj instanceof Map)) {
            return ResponseEntity.badRequest().body("{\"ok\":false,\"reason\":\"NOT_ISSUED\"}");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> saved = (Map<String, Object>) obj;
        String savedCode = (String) saved.get("code");
        Instant issuedAt = (Instant) saved.get("issuedAt");

        // 5분 만료
        if (issuedAt == null || Duration.between(issuedAt, Instant.now()).toMinutes() >= 5) {
            session.removeAttribute(key);
            return ResponseEntity.badRequest().body("{\"ok\":false,\"reason\":\"EXPIRED\"}");
        }
        if (!code.equals(savedCode)) {
            return ResponseEntity.badRequest().body("{\"ok\":false,\"reason\":\"MISMATCH\"}");
        }

        // 일치 : 검증 완료 표시(필요 시 플래그 저장)
        session.setAttribute("EMAIL_CERT_VERIFIED::" + email, true);
        // 원한다면 코드 제거(재사용 방지)
        session.removeAttribute(key);

        return ResponseEntity.ok("{\"ok\":true}");
    }
}
