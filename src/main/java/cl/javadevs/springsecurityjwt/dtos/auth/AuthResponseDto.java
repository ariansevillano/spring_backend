package cl.javadevs.springsecurityjwt.dtos.auth;

import lombok.Data;

//Esta clase va a ser la que nos devolverá la información con el token y el tipo que tenga este
@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
