package cl.javadevs.springsecurityjwt.exceptions;

public class UsuarioExistenteException extends RuntimeException {
    public UsuarioExistenteException (String message) {
        super(message);
    }
}
