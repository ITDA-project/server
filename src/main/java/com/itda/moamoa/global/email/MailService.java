package com.itda.moamoa.global.email;

import com.itda.moamoa.domain.user.entity.SnsDiv;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.email.dto.PasswordDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final String senderEmail;
    private final UserRepository userRepository;

    //메모리 저장소가 아닌, DB에 이메일,인증번호,만료시간을 넣어서 체크할 수 있음
    //현재는 {이메일:인증번호} Map 으로 메모리 저장소 사용
    private final Map<String,Integer> otpNumbers = new ConcurrentHashMap<>();

    @Autowired
    public MailService(JavaMailSender javaMailSender,@Value("${spring.mail.username}") String senderEmail,UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
        this.userRepository = userRepository;
    }

    public static int createNumber() {
        return ((int)(Math.random() * 90000)) + 100000;
    }

    @Async
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

    /**
     * DB에 없는 이메일 이라면 Exception 발생
     * 소셜 로그인한 유저라면 비밀번호 찾기 불가능
     * 자체 회원가입한 유저만 비밀번호 찾기 가능
     */
    public void checkExistEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("요청한 " + email + "을 찾을 수 없습니다."));
        SnsDiv userSns = user.getSnsDiv();
        if(SnsDiv.NAVER.equals(userSns)||SnsDiv.KAKAO.equals(userSns))
            throw new EntityNotFoundException("요청한 " + email + "을 찾을 수 없습니다.");
    }

    private int getOtpNumber(String email){
        return otpNumbers.getOrDefault(email, -1);
    }

    public Boolean checkOtp(PasswordDto passwordDto) {
        int otpNumber = getOtpNumber(passwordDto.getEmail());
        return otpNumber == passwordDto.getOtpNumber();
    }

}
