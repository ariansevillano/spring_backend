package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.dtos.valoracion.request.DtoValoracion;
import cl.javadevs.springsecurityjwt.dtos.valoracion.response.DtoValoracionResponse;
import cl.javadevs.springsecurityjwt.models.Valoracion;
import cl.javadevs.springsecurityjwt.services.ValoracionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valoracion/")
@RequiredArgsConstructor
public class RestControllerValoracion {
    private ValoracionService valoracionService;

    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> crearValoracion(@RequestBody @Valid DtoValoracion dtoValoracion, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }

        valoracionService.crear(dtoValoracion,authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.succes("Servicio creado correctamente", null)
        );
    }

    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoValoracionResponse>>>listarValoracion(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoValoracionResponse> valoraciones = valoracionService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de valoraciones obtenida correctamente",valoraciones));
    }
}
