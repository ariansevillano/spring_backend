package cl.javadevs.springsecurityjwt.dtos.auth;

import lombok.Data;

@Data
public class DtoRegistro {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
}
