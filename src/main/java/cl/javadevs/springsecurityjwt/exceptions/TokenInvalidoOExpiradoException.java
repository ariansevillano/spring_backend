package cl.javadevs.springsecurityjwt.exceptions;

public class TokenInvalidoOExpiradoException extends RuntimeException{
    public TokenInvalidoOExpiradoException (String message){
        super(message);
    }

}
