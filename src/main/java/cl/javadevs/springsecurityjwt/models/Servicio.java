package cl.javadevs.springsecurityjwt.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicios")
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servicio_id")
    private Long servicio_id;
    @Column(nullable = false)
    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String nombre;
    @Column(nullable = false)
    @NotNull(message = "El campo precio no puede estar vacío")
    private Long precio;
    @Column(nullable = false)
    @NotBlank(message = "El campo descripción no puede estar vacío")
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "tipoServicio_id")
    @Column(nullable = false)
    @NotNull(message = "El campo tipoServicio no puede estar vacío")
    private TipoServicio tipoServicio;
}
