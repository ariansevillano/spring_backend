package cl.javadevs.springsecurityjwt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.StyledEditorKit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Valoraciones")
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "valoracion_id")
    private Long valoracion_id;
    private Integer valoracion;
    private Boolean util;
    private String mensaje;
    @ManyToOne
    @JoinColumn( name = "usuario_id")
    @Column(nullable = false)
    private Usuario usuario;

}
