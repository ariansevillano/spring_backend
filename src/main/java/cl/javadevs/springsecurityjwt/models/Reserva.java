package cl.javadevs.springsecurityjwt.models;

import cl.javadevs.springsecurityjwt.util.EstadoReserva;
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
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id")
    private Long reserva_id;

    // Relación con Barbero
    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;

    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación con HorarioRango
    @ManyToOne
    @JoinColumn(name = "horarioRango_id", nullable = false)
    private HorarioRango horarioRango;

    // Estado de la reserva
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    // Motivo (opcional)
    @Column(nullable = true)
    private String motivoDescripcion;

    // Adicionales (opcional)
    @Column(nullable = true)
    private String adicionales;

    // Fecha de creación de la reserva
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    // Fecha para la que se reserva el servicio
    @Column(nullable = false)
    private LocalDate fechaReserva;

    // URL de pago (opcional)
    @Column(nullable = true)
    private String urlPago;
}
