package cl.javadevs.springsecurityjwt.dtos.horarioRangos.response;

import cl.javadevs.springsecurityjwt.models.TipoHorario;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DtoHorarioRangoResponse {
    private Long horarioRango_id;
    private String rango;
    private String tipoHorario;
}
