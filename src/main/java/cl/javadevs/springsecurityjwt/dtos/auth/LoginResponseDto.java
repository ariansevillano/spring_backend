package cl.javadevs.springsecurityjwt.dtos.auth;

public class LoginResponseDto {
    private String token;
    private String rol;

    public LoginResponseDto(String token, String rol) {
        this.token = token;
        this.rol = rol;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}