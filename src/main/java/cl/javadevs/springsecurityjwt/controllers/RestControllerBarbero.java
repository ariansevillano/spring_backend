package cl.javadevs.springsecurityjwt.controllers;


import cl.javadevs.springsecurityjwt.dtos.barbero.request.DtoBarbero;
import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoResponse;
import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.services.BarberoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbero/")
@RequiredArgsConstructor
public class RestControllerBarbero {
    private BarberoService barberoService;

    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> crearBarbero(@RequestBody @Valid DtoBarbero dtoBarbero, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }

        barberoService.crear(dtoBarbero);
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
    public ResponseEntity<ApiResponse<DtoBarberoResponse>> obtenerBarberoPorId(@PathVariable Long id) {
        DtoBarberoResponse dtoBarberoResponse = barberoService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Barbero no encontrado",dtoBarberoResponse));
    }

    @PutMapping(value = "actualizar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarBarbero(@PathVariable Long id,@RequestBody @Valid DtoBarbero dtoBarbero) {
        barberoService.update(id,dtoBarbero);
        return ResponseEntity.ok(ApiResponse.succes("Barbero Actualizado exitosamente",dtoBarbero));
    }

    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> eliminarBarbero(@PathVariable Long id) {
        barberoService.deshabilitar(id);
        return ResponseEntity.ok(ApiResponse.succes("Barbero Deshabilitado exitosamente",null));
    }

}
