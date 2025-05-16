package cl.javadevs.springsecurityjwt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "horario_barbero_base",uniqueConstraints = {@UniqueConstraint(columnNames = {"barbero_id","tipoHorario_id","dia"})})
public class HorarioBarberoBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horario_barbero_base_id")
    private Long horarioBarberoBase_id;
    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;
    @ManyToOne
    @JoinColumn(name = "tipoHorario_id", nullable = false)
    private TipoHorario tipoHorario;
    @Column(nullable = false)
    private String dia;
    //hace referenica a activo en el horario o descanso, o sea 1 = trabaja en dicho horario, null = descansa
    private Integer est_id;
    //hace referencia a trabajador activado, o sea 1 = normal, 0 = apagado o despedido para ya no usarse
    private Integer estado;
}
