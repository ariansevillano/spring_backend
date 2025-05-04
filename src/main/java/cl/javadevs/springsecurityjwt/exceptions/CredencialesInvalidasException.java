package cl.javadevs.springsecurityjwt.exceptions;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException (String message) {
        super(message);
    }
}
