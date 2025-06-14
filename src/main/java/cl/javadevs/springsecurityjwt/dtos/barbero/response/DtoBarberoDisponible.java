package cl.javadevs.springsecurityjwt.dtos.barbero.response;

import lombok.Data;

@Data
public class DtoBarberoDisponible {
    private Long barberoId;
    private String nombre;
    private String urlBarbero;
    private boolean disponible;
}
