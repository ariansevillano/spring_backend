package cl.javadevs.springsecurityjwt.dtos.auth.request;

import lombok.Data;

@Data
public class DtoRegistro {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
}
