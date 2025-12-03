package ro.msg.learning.shop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.Order;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final Environment env;

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    private final TemplateParserContext templateParserContext = new TemplateParserContext();

//    @Value("${email.confirmation.subject}")
//    private String subjectTemplate;
//
//    @Value("${email.confirmation.text}")
//    private String textTemplate;
//
//    @Value("${email.confirmation.html}")
//    private String htmlTemplate;

    public void sendOrderConfirmationMail(String to, Order order) throws MessagingException {
        String subjectTemplate = env.getProperty("email.confirmation.subject");
        String textTemplate = env.getProperty("email.confirmation.text");
        String htmlTemplate = env.getProperty("email.confirmation.html");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        String subject = expressionParser.parseExpression(subjectTemplate, templateParserContext).getValue(order, String.class);
        String text = expressionParser.parseExpression(textTemplate, templateParserContext).getValue(order, String.class);
        String html = expressionParser.parseExpression(htmlTemplate, templateParserContext).getValue(order, String.class);

        helper.setSubject(subject);
        helper.setText(text, html);
        helper.setTo(to);
        helper.setFrom(to);

        mailSender.send(message);
    }
}
