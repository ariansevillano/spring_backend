package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoLogin;
import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoRefreshToken;
import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoRegistro;
import cl.javadevs.springsecurityjwt.dtos.auth.response.DtoLoginResponse;
import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.dtos.auth.request.DtoResetPassword;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class RestControllerAuth {

    private AuthService authService;

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
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody @Valid DtoLogin dtoLogin) {
        try {
            DtoLoginResponse dtoLoginResponse = authService.login(dtoLogin);
            return ResponseEntity.ok(ApiResponse.succes("Inicio de sesión existoso", dtoLoginResponse));
        } catch (Exception e) {
            // Manejar errores de autenticación
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse
                    .error("Credenciales Inválidas",null) );
        }
    }


    @PostMapping("resetPassword")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody @Valid DtoResetPassword dtoResetPassword){
        authService.resetPassword(dtoResetPassword);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.succes("Contraseña cambiada correctamente",null)
        );
    }

    @PostMapping("refreshToken")
    public ResponseEntity<ApiResponse<Object>> refreshToken(@RequestBody @Valid DtoRefreshToken dtoRefreshToken){
        String newToken = authService.renovarToken(dtoRefreshToken);
        String newRefreshToken = authService.renovarRefreshToken(dtoRefreshToken.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.succes("Token renovado exitosamente.", Map.of(
                        "token", newToken,
                        "refreshToken",newRefreshToken
                ))
        );
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse<Object>> logout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("No estás autenticado.", null)
            );
        }

        String username = authentication.getName();
        authService.logout(username);

        return ResponseEntity.ok(ApiResponse.succes("Logout exitoso.", null));
    }
}
