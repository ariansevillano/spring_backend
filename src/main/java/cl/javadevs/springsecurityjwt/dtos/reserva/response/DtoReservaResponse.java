package cl.javadevs.springsecurityjwt.dtos.reserva.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DtoReservaResponse {
    private Long reservaId;
    private String barberoNombre;
    private String usuarioNombre;
    private String horarioRango; // Puedes poner el rango como texto, ej: "09:00-10:00"
    private String estado;
    private String motivoDescripcion; // Lo pone el admin
    private String adicionales;       // Lo pone el usuario
    private LocalDateTime fechaCreacion;
    private LocalDate fechaReserva;
    private String urlPago;
}