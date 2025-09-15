package edu.example.tripnote.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.example.tripnote.dao.UserDAO;
import edu.example.tripnote.domain.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserDAO dao;
    private final MailService mailService;

    @Transactional
    public boolean resetPasswordAndNotifyPlain(String username, String email) {
        if (isBlank(username) || isBlank(email)) return false;

        UserDTO user = dao.searchPassword(username, email);
        if (user == null) return false;

        String tempPw = generateTempPassword(8);     // 임시 비번 생성
        int updated = dao.updatePassword(user.getId(), tempPw);
        if (updated != 1) throw new IllegalStateException("Password update failed");

        // 메일 발송 (텍스트/HTML 중 선택)
        String subject = "[Tripnote] 임시 비밀번호 안내";
        String body = user.getUsername() + "님,\n\n임시 비밀번호는 아래와 같습니다.\n" +
                      "임시 비밀번호: " + tempPw + "\n\n" +
                      "로그인 후 반드시 비밀번호를 변경해 주세요.";
        mailService.sendText(email.trim(), subject, body);

        return true;
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private String generateTempPassword(int length) {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%^*";
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }
}
