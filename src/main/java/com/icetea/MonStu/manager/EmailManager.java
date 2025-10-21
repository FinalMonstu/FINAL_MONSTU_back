package com.icetea.MonStu.manager;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmailManager {

    private final JavaMailSender sender;

    //  이메일 인증 번호 전송
    public String sendVerificationEmailCode(String email) {
//        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes()))
//                .getRequest().getSession();

        String randomCode = getRandomText();
//        session.setAttribute("randomCode", randomCode);

        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject("MonStu 이메일 인증");
            mimeMessage.setContent(
                    "<div>MonStu 이메일 인증 번호</div><h3>" + randomCode + "</h3><div>사이트로 이동해주세요</div>",
                    "text/html;charset=utf-8"
            );
            sender.send(mimeMessage);
            return randomCode;
        } catch (MessagingException e) {
            throw new RuntimeException("코드 전송에 실패했습니다.");
        }
    }

    private String getRandomText() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 숫자
        return String.valueOf(code);
    }
}
