package com.itda.moamoa.global.email;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final String senderEmail;

    private final Map<String,Integer> otpNumbers = new ConcurrentHashMap<>();

    @Autowired
    public MailService(JavaMailSender javaMailSender,@Value("${spring.mail.username}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }

    public static int createNumber() {
        return ((int)(Math.random() * 90000)) + 100000;
    }

    public void sendMail(String mail){
        int number = createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        otpNumbers.put(mail, number);

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO,mail);
            message.setSubject("모아모아 이메일 인증번호");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public int getOtpNumber(String email){
        return otpNumbers.getOrDefault(email, -1);
    }

    public Boolean checkOtp(PasswordDto passwordDto) {
        int otpNumber = getOtpNumber(passwordDto.getEmail());
        return otpNumber == passwordDto.getOtpNumber();
    }

}
