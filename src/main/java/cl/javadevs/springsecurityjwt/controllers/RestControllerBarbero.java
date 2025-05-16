package cl.javadevs.springsecurityjwt.controllers;


import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.models.Barbero;
import cl.javadevs.springsecurityjwt.models.Servicio;
import cl.javadevs.springsecurityjwt.services.BarberoService;
import cl.javadevs.springsecurityjwt.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbero/")
public class RestControllerBarbero {
    private BarberoService barberoService;
    @Autowired
    public RestControllerBarbero(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> crearBarbero(@RequestBody @Valid Barbero barbero, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }

        barberoService.crear(barbero);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.succes("Servicio creado correctamente", null)
        );
    }

    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<Barbero>>> listarBarbero(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<Barbero> barberos = barberoService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de barberos obtenida correctamente",barberos));
    }

    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> obtenerBarberoPorId(@PathVariable Long id) {
        Barbero barbero = barberoService.readOne(id).orElseThrow();
        return ResponseEntity.ok(ApiResponse.succes("Barbero no encontrado",barbero));
    }

    @PutMapping(value = "actualizar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarBarbero(@RequestBody Barbero barbero) {
        barberoService.update(barbero);
        return ResponseEntity.ok(ApiResponse.succes("Barbero Actualizado exitosamente",barbero);
    }

    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> eliminarBarbero(@PathVariable Long id) {
        barberoService.deshabilitar(id);
        return ResponseEntity.ok(ApiResponse.succes("Barbero Deshabilitado exitosamente",null));
    }

}
