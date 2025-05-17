package cl.javadevs.springsecurityjwt.exceptions;

import cl.javadevs.springsecurityjwt.models.TipoHorario;

public class TipoHorarioNoEncotradoException extends RuntimeException {
    public TipoHorarioNoEncotradoException (String message) {
        super(message);
    }
}
