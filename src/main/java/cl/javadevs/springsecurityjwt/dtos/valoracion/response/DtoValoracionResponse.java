package cl.javadevs.springsecurityjwt.dtos.valoracion.response;

import lombok.Data;

@Data
public class DtoValoracionResponse {
    private Long valoracion_id;
    private Integer valoracion;
    private Boolean util;
    private String mensaje;
    private String usuario_nombre;
}
