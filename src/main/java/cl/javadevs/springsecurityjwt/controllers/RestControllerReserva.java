package cl.javadevs.springsecurityjwt.controllers;

import cl.javadevs.springsecurityjwt.dtos.barbero.response.DtoBarberoDisponible;
import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import cl.javadevs.springsecurityjwt.dtos.reserva.request.DtoReserva;
import cl.javadevs.springsecurityjwt.dtos.reserva.response.DtoReservaResponse;
import cl.javadevs.springsecurityjwt.models.Usuario;
import cl.javadevs.springsecurityjwt.services.ReservaService;
import cl.javadevs.springsecurityjwt.util.EstadoReserva;
import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/reserva/")
@RequiredArgsConstructor
public class RestControllerReserva {

    private final ReservaService reservaService;
    @GetMapping("barberos-disponibles")
    public ResponseEntity<List<DtoBarberoDisponible>> listarBarberosDisponibles(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("tipoHorarioId") Long tipoHorarioId,
            @RequestParam("horarioRangoId") Long horarioRangoId) {
        List<DtoBarberoDisponible> barberos = reservaService.listarBarberosDisponibles(fecha, tipoHorarioId, horarioRangoId);
        return ResponseEntity.ok(barberos);
    }

    @PostMapping("crear")
    public ResponseEntity<ApiResponse<Object>> crearReserva(
            @RequestBody DtoReserva dto,
            Authentication authentication) {
        reservaService.crearReserva(dto, authentication, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes("Reserva creada correctamente", null));
    }

    @PostMapping("subir-comprobante/{reservaId}")
    public ResponseEntity<ApiResponse<Object>> subirComprobante(
            @PathVariable Long reservaId,
            @RequestPart("imagen") MultipartFile imagen,
            Authentication authentication) {
        reservaService.subirComprobante(reservaId, imagen, authentication);
        return ResponseEntity.ok(ApiResponse.succes("Comprobante subido", null));
    }

    @GetMapping("admin/listar")
    public ResponseEntity<List<DtoReservaResponse>> listarReservas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false) Long usuarioId ) {
        List<DtoReservaResponse> reservas = reservaService.listarReservas(fecha, estado,usuarioId);
        return ResponseEntity.ok(reservas);
    }


    @PutMapping("admin/cambiar-estado/{reservaId}")
    public ResponseEntity<ApiResponse<Object>> cambiarEstado(
            @PathVariable Long reservaId,
            @RequestParam("estado") EstadoReserva estado,
            @RequestParam(value = "motivoDescripcion", required = false) String motivoDescripcion) {
        reservaService.cambiarEstado(reservaId, estado, motivoDescripcion);
        return ResponseEntity.ok(ApiResponse.succes("Estado actualizado", null));
    }


    @GetMapping("mis-reservas")
    public ResponseEntity<List<DtoReservaResponse>> listarMisReservas(Authentication authentication) {
        List<DtoReservaResponse> reservas = reservaService.listarReservasPorUsuario(authentication);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("consultarRecompensa")
    public ResponseEntity<ApiResponse<Boolean>> consultarRecompensa(Authentication authentication) {
        Boolean estado = reservaService.buscarReservasRecompensa(authentication);
        return ResponseEntity.ok(ApiResponse.succes("Estado enviado",estado));
    }

    @PostMapping("crearReservaRecompensa")
    public ResponseEntity<ApiResponse<Object>> crearReservaRecompensa(
            @RequestBody DtoReserva dto,
            Authentication authentication) {
        reservaService.crearReservaRecompensa(dto,authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes("Reserva creada correctamente", null));
    }
}
