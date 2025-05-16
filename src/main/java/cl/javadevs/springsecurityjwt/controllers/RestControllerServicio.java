package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.dtos.servicio.request.DtoServicio;
import cl.javadevs.springsecurityjwt.dtos.servicio.response.DtoServicioResponse;
import cl.javadevs.springsecurityjwt.exceptions.ServicioNoEncontradoException;
import cl.javadevs.springsecurityjwt.models.Servicio;
import cl.javadevs.springsecurityjwt.services.ServicioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicio/")
public class RestControllerServicio {
    private ServicioService servicioService;
    @Autowired
    public RestControllerServicio(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    //Petición para crear un  servicio
    @PostMapping(value = "crear", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> crearServicio(@RequestBody @Valid DtoServicio dtoServicio, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        servicioService.crear(dtoServicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.succes("Servicio creado correctamente", null)
        );
    }

    //Petición para obtener todos los servicio en la BD
    @GetMapping(value = "listar", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<List<DtoServicioResponse>>> listarServicio(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.error("El token es inválido o ha expirado. Por favor, inicia sesión nuevamente.", null)
            );
        }
        List<DtoServicioResponse> dtoServicios = servicioService.readAll();
        return ResponseEntity.ok(ApiResponse.succes("Lista de servicios obtenida correctamente",dtoServicios));
    }

    //Petición para obtener servicio mediante "ID"
    @GetMapping(value = "listarId/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<DtoServicioResponse>> obtenerServicioPorId(@PathVariable Long id) {
        DtoServicioResponse dtoServicio = servicioService.readOne(id);
        return ResponseEntity.ok(ApiResponse.succes("Servicio no encontrado",dtoServicio));
    }

    //Petición para actualizar un servicio
    @PutMapping(value = "actualizar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> actualizarServicio(@PathVariable Long id ,@RequestBody DtoServicio dtoServicio) {
        servicioService.update(id,dtoServicio);
        return ResponseEntity.ok(ApiResponse.succes("Servicio Actualizado exitosamente",dtoServicio));
    }

    //Petición para eliminar un servicio por "Id"
    @DeleteMapping(value = "eliminar/{id}", headers = "Accept=application/json")
    public ResponseEntity<ApiResponse<Object>> eliminarServicio(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.ok(ApiResponse.succes("Servicio Eliminado exitosamente",null));
    }
}
