package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.barbero.request.DtoBarbero;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoResponse;
import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.dtos.usuario.request.DtoUsuario;
import cl.javadevs.springsecurityjwt.dtos.usuario.response.DtoUsuarioResponse;
import cl.javadevs.springsecurityjwt.exceptions.ImagenNoSubidaException;
import cl.javadevs.springsecurityjwt.services.UsuarioService;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/usuario/")
@RequiredArgsConstructor
public class RestControllerUsuario {
    private final UsuarioService usuarioService;
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoUsuarioResponse>>> listarUsuarios(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoUsuarioResponse> usuarios = usuarioService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de usuarios obtenida correctamente",usuarios));
    }

    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoUsuarioResponse>> obtenerUsuarioPorId(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        DtoUsuarioResponse usuario = usuarioService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Usuario encontrado",usuario));
    }

    @GetMapping(value = "listarme", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoUsuarioResponse>> obtenerMiUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        DtoUsuarioResponse usuario = usuarioService.readOneByAuth(authentication);
        return ResponseEntity.ok(ApiResponse.succes("Usuario encontrado",usuario));
    }

    @PutMapping(value = "actualizar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarUsuario(@PathVariable Long id,@RequestPart @Valid DtoUsuario dtoUsuario,
                                                                 @RequestPart (value = "imagen", required = false) MultipartFile imagen,
                                                                 Authentication authentication) {
        if (imagen != null &&
                (imagen.getContentType() == null
                        || !imagen.getContentType().startsWith("image/"))) {
            throw new ImagenNoSubidaException(MensajeError.TIPO_ARCHIVO_NO_PERMITIDO);
        }
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        usuarioService.update(id,dtoUsuario,imagen);
        DtoUsuarioResponse dtoResponse = usuarioService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Usuario Actualizado exitosamente",dtoResponse));
    }


}
