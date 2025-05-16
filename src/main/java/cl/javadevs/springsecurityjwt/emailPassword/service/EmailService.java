package cl.javadevs.springsecurityjwt.emailPassword.service;

import cl.javadevs.springsecurityjwt.emailPassword.dto.EmailDto;
import cl.javadevs.springsecurityjwt.exceptions.EmailNoEnviadoException;
import cl.javadevs.springsecurityjwt.exceptions.UsuarioExistenteException;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.repositories.IUsuariosRepository;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service

public class EmailService {
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    private IUsuariosRepository usuariosRepository;
    @Autowired
    public EmailService (JavaMailSender javaMailSender, TemplateEngine templateEngine,
                         IUsuariosRepository usuariosRepository){
        this.javaMailSender=javaMailSender;
        this.templateEngine=templateEngine;
        this.usuariosRepository=usuariosRepository;
    }

    @Value("${mail.urlFront}")
    private String urlFront;

    @Value("${spring.mail.username}")
    private String mailFrom;

    private final static String SUBJECT = "Cambio de contraseña";

    public void enviarCorreo(EmailDto emailDto){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("userName", emailDto.getNombre());
            model.put("url", urlFront + "?token=" + emailDto.getTokenPassword());

            context.setVariables(model);
            String htmlText =  templateEngine.process("email-template",context);
            helper.setFrom(emailDto.getMailFrom());
            helper.setTo(emailDto.getMailTo());
            helper.setSubject(emailDto.getSubject());
            helper.setText(htmlText,true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailNoEnviadoException(MensajeError.CORREO_NO_ENVIADO);
        }
    }

    public void procesarEnvioCorreo(EmailDto emailDto){
        //validar si el usuario existe
        Usuario usuario =usuariosRepository.findByUsername(emailDto.getUsername())
                .orElseThrow(() -> new UsuarioExistenteException(MensajeError.USUARIO_NO_EXISTENTE));

        if (!usuario.getEmail().equals(emailDto.getMailTo()))
            throw new EmailNoEnviadoException(MensajeError.CORREO_NO_COINCIDE);

        // Verificar si ya se generó un token recientemente
        if (usuario.getLastTokenRequest() != null && usuario.getLastTokenRequest().isAfter(LocalDateTime.now().minusMinutes(5))) {
            throw new EmailNoEnviadoException(MensajeError.CORREO_RECIENTE);
        }

        //Generamos el token de restablecimiento y actualizamos el campo
        String tokenPassword = UUID.randomUUID().toString();
        usuario.setTokenPassword(tokenPassword);
        usuario.setLastTokenRequest(LocalDateTime.now());
        usuariosRepository.save(usuario);

        //Configuramos el dto para el correo
        emailDto.setMailFrom(mailFrom);
        emailDto.setMailTo(usuario.getEmail());
        emailDto.setSubject(SUBJECT);
        emailDto.setUsername(usuario.getUsername());
        emailDto.setNombre(usuario.getNombre());
        emailDto.setTokenPassword(tokenPassword);

        //enviamos el correo
        enviarCorreo(emailDto);
    }
}
