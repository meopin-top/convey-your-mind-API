package muffintop.cym.api.service;

import java.time.LocalDateTime;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.domain.MagicLink;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.repository.MagicLinkRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final MagicLinkRepository magicLinkRepository;

    @Value("${host.name}")
    private String host;

    public String makeMagicLink() {
        int leftLimit = 48; // 0
        int rightLimit = 122; // z
        int targetStringLength = 10;

        Random random = new Random();

        String generatedString;

        while (true) {
            generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
            if (!magicLinkRepository.existsById(generatedString)) {
                break;
            }
        }
        return generatedString;
    }

    @Async
    public void sendMail(String email, User user) throws MessagingException {
        Context context = new Context();


        MagicLink magicLink = MagicLink.builder()
            .id(makeMagicLink())
            .user(user)
            .expiredDatetime(LocalDateTime.now().plusDays(90))
            .build();

        magicLinkRepository.save(magicLink);

        context.setVariable("nickname", user.getNickName());
        context.setVariable("userId", user.getId());
        context.setVariable("host", host+"/api/magic-link/"); // 메시지소스로 설정해두고 받아쓰면 참 편하다.
        context.setVariable("link", magicLink.getId()); // 인증을 진행할 링크


        String message = templateEngine.process("email.html", context);

        MimeMessage mail = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8"); // 2번째 인자는 Multipart여부 결정
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("[마음을 전해요] 회원가입이 완료되었습니다.");
        mimeMessageHelper.setText(message, true); // 2번째 인자는 HTML여부 결정
        emailSender.send(mail);
    }
}
