package com.example.email_send_test.service;

import com.example.email_send_test.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service

public class EmailService {
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    @Autowired
    public EmailService (JavaMailSender javaMailSender, TemplateEngine templateEngine){
        this.javaMailSender=javaMailSender;
        this.templateEngine=templateEngine;
    }

    @Value("${mail.urlFront}")
    private String urlFront;

    public void sendEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ariansevillano1@gmail.com");
        message.setTo("2123110074@untels.edu.pe");
        message.setSubject("Email de prueba, envío simple");
        message.setText("Contenido del email de prueba");

        javaMailSender.send(message);
    }

    public void sendEmailTemplate(EmailDto emailDto){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("userName", emailDto.getUsername());
            model.put("url",urlFront+emailDto.getJwt());

            context.setVariables(model);
            String htmlText =  templateEngine.process("Email-Template",context);
            helper.setFrom(emailDto.getMailFrom());
            helper.setTo(emailDto.getMailTo());
            helper.setSubject(emailDto.getSubject());
            helper.setText(htmlText,true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
