package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.auth.DtoLogin;
import cl.javadevs.springsecurityjwt.dtos.auth.DtoRegistro;
import cl.javadevs.springsecurityjwt.dtos.auth.LoginResponseDto;
import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class RestControllerAuth {

    private AuthService authService;

    @Autowired

    public RestControllerAuth(AuthService authService) {
        this.authService = authService;
    }

    //endpoint mejorado para crear un usuario tipo USER
    @PostMapping("register")
    public ResponseEntity<ApiResponse<Object>> registrar(@RequestBody DtoRegistro dtoRegistro, Authentication authentication){

        // Validar que el usuario autenticado tenga el rol ADMIN
        if (authentication.getAuthorities().stream().noneMatch(auth ->auth.getAuthority().equals("ADMIN"))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Acceso denegado: Solo los administradores pueden registrar usuarios", null)
            );
        }
        try {
            //Delegamos lógica al servicio
            authService.registrarUsuario(dtoRegistro);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(HttpStatus.OK.value(), "Registro de usuario cliente exitoso", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(),null)
            );
        }
    }


    //Método para poder guardar usuarios de tipo ADMIN con token
    @PostMapping("v1/registerAdm")
    public ResponseEntity<ApiResponse<Object>> registrarAdmin(@RequestBody DtoRegistro dtoRegistro, Authentication authentication ) {

        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Acceso denegado: solo los administradores pueden registrar usuario", null)
            );
        }
        try {
            //Delegamos lógica al servicio
            authService.registrarAdministrador(dtoRegistro);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(HttpStatus.OK.value(), "Registro de usuario administrador exitoso", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(),null)
            );
        }

    }

    //Método para poder guardar usuarios de tipo ADMIN sin token
    @PostMapping("v2/registerAdm")
    public ResponseEntity<ApiResponse<Object>> registrarAdmin2(@RequestBody DtoRegistro dtoRegistro, Authentication authentication ) {
        try {
            //Delegamos lógica al servicio
            authService.registrarAdministrador(dtoRegistro);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(HttpStatus.OK.value(), "Registro de usuario administrador exitoso", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(),null)
            );
        }

    }

    //endpoint para login
    @PostMapping("login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody DtoLogin dtoLogin) {
        try {
            LoginResponseDto loginResponseDto = authService.login(dtoLogin);
            return ResponseEntity.ok(ApiResponse.succes("Inicio de sesión existoso",loginResponseDto));
        } catch (Exception e) {
            // Manejar errores de autenticación
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse
                    .error("Credenciales Inválidas",null) );
        }
    }

}
