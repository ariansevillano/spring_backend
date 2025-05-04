package cl.javadevs.springsecurityjwt.dtos.auth;

import lombok.Data;

@Data
public class DtoLogin {
    private String username;
    private String password;
}
