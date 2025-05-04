package cl.javadevs.springsecurityjwt.exceptions;

public class ServicioNoEncontradoException extends  RuntimeException {
    public ServicioNoEncontradoException (String message) {
        super(message);
    }
}
