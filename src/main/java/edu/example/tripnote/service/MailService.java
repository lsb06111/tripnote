package edu.example.tripnote.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private static final String FROM = "iamjdy12@gmail.com"; // Gmail 계정과 동일 권장

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendText(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom(FROM);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    public void sendHtml(String to, String subject, String html) throws MessagingException {
        MimeMessage mm = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mm, false, "UTF-8");
        helper.setTo(to);
        helper.setFrom(FROM);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(mm);
    }
}
