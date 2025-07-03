package cl.javadevs.springsecurityjwt.emailPassword.controller;

import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.emailPassword.dto.EmailDto;
import cl.javadevs.springsecurityjwt.emailPassword.service.EmailService;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.repositories.IUsuariosRepository;
import cl.javadevs.springsecurityjwt.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/emailPassword/")
@RequiredArgsConstructor
@CrossOrigin
public class EmailController {

    private final EmailService emailService;

    @PostMapping("sendEmail")
    public ResponseEntity<ApiResponse<Object>> sendEmail(@RequestBody EmailDto emaildto){
        emailService.procesarEnvioCorreo(emaildto);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.succes("Correo enviado con éxito a su email asociado, recuerde revisar su carpeta de spam.",null)
        );

    }


}
