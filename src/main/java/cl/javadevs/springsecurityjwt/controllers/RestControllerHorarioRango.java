package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.dtos.horarioRangos.response.DtoHorarioRangoResponse;
import cl.javadevs.springsecurityjwt.dtos.servicio.response.DtoServicioResponse;
import cl.javadevs.springsecurityjwt.services.HorarioRangoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rango/")
@RequiredArgsConstructor
public class RestControllerHorarioRango {

    private final HorarioRangoService horarioRangoService;

    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoHorarioRangoResponse>>> listarRangos(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoHorarioRangoResponse> dtoHorarioRangoResponses = horarioRangoService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de rangos obtenida correctamente",dtoHorarioRangoResponses));
    }

    //Petición para obtener servicio mediante "ID"
    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoHorarioRangoResponse>> obtenerRangoPorId(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        DtoHorarioRangoResponse dtoRango = horarioRangoService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Rango encontrado",dtoRango));
    }
}
