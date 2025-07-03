package cl.javadevs.springsecurityjwt.controllers;


import cl.javadevs.springsecurityjwt.dtos.barbero.request.DtoBarbero;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoDisponible;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoResponse;
import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.exceptions.ImagenNoSubidaException;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.services.BarberoService;
import cl.javadevs.springsecurityjwt.util.MensajeError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/barbero/")
@RequiredArgsConstructor
public class RestControllerBarbero {
    private final BarberoService barberoService;

    @PostMapping(value = "crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> crearBarbero(@RequestPart("dtoBarbero") @Valid DtoBarbero dtoBarbero,
                                                            @RequestPart(value="imagen",required = false) MultipartFile imagen,
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
        barberoService.crear(dtoBarbero,imagen);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.succes("Barbero creado correctamente", null)
        );
    }

    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoBarberoResponse>>> listarBarbero(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoBarberoResponse> barberos = barberoService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de barberos obtenida correctamente",barberos));
    }

    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoBarberoResponse>> obtenerBarberoPorId(@PathVariable Long id,Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        DtoBarberoResponse dtoBarberoResponse = barberoService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Barbero encontrado",dtoBarberoResponse));
    }

    @PutMapping(value = "actualizar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarBarbero(@PathVariable Long id,@RequestPart @Valid DtoBarbero dtoBarbero,
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
        barberoService.update(id,dtoBarbero,imagen);
        DtoBarberoResponse dtoResponse = barberoService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Barbero Actualizado exitosamente",dtoResponse));
    }

    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> eliminarBarbero(@PathVariable Long id,Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        barberoService.deshabilitar(id);
        return ResponseEntity.ok(ApiResponse.succes("Barbero Deshabilitado exitosamente",null));
    }

}
