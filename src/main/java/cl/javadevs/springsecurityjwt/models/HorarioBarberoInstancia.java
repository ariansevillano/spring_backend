package cl.javadevs.springsecurityjwt.models;

import cl.javadevs.springsecurityjwt.util.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "horario_barbero_instancias")
public class HorarioBarberoInstancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarioBarberoInstancia_id")
    private Long horarioBarberoInstancia_id;
    @ManyToOne
    @JoinColumn(name = "barbero_id",nullable = false)
    private Barbero barbero;
    @ManyToOne
    @JoinColumn(name = "tipoHorario_id",nullable = false)
    private TipoHorario tipoHorario;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;
    @Column(nullable = false)
    private LocalDate fechaInicio;
    private Integer est_id;
}
