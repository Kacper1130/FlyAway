package FlyAway.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailAddress;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);


    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailAddress);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);
    }

    @Async
    public void sendConfirmationEmail(String to, String username, String verificationLink) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("verificationLink", verificationLink);

        Context context = new Context();
        context.setVariables(properties);

        String template = templateEngine.process("email_verification", context);

        helper.setFrom(emailAddress);
        helper.setTo(to);
        helper.setSubject("Confirm your email");
        helper.setText(template, true);
        javaMailSender.send(mimeMessage);
        LOGGER.info("Confirmation email has been sent to {}", to);
    }

}
