package muffintop.cym.api.service;

import javax.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;

    @Async
    public void sendMail(String email) throws MessagingException {
        SimpleMailMessage message  = new SimpleMailMessage ();
        message.setTo(email);
        message.setSubject("환영합니다");
        message.setText("축하합니다");
        emailSender.send(message);
    }
}
