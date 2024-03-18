package uz.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String token, String message){
        super(MessageFormat.format("Error: token: {0}, message: {1}", token, message));
    }
}
