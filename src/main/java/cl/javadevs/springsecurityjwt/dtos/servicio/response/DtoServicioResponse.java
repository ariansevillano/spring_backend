package cl.javadevs.springsecurityjwt.dtos.servicio.response;

import cl.javadevs.springsecurityjwt.models.TipoServicio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DtoServicioResponse {
    private Long servicio_id;
    private String nombre;
    private Long precio;
    private String descripcion;
    private String nombre_tipoServicio;
    private String urlServicio;
}
