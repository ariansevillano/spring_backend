package cl.javadevs.springsecurityjwt.exceptions;

import cl.javadevs.springsecurityjwt.dtos.common.ApiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsuarioExistenteException(UsuarioExistenteException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Error de ejecución: " + ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RolNoEncontradoException.class)
    public ResponseEntity<ApiResponse<Object>> handleRolNoEncontradoException(RolNoEncontradoException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiResponse<Object>> handleCredencialesInvalidasException(CredencialesInvalidasException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServicioNoEncontradoException.class)
    public ResponseEntity<ApiResponse<Object>> handleServicioNoEncontradoException(ServicioNoEncontradoException ex){
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNoEnviadoException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailNoEnviadoException(EmailNoEnviadoException ex){
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenInvalidoOExpiradoException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenInvalidoOExpiradoException(TokenInvalidoOExpiradoException ex){
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
