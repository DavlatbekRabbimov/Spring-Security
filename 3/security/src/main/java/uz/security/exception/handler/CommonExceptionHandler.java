package uz.security.exception.handler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import uz.security.exception.NotFoundException;
import uz.security.exception.RefreshTokenException;
import uz.security.exception.BadRequestException;
import uz.security.service.ExceptionBuilderService;
import uz.security.exception.message.ErrorResponse;

@AllArgsConstructor
@RestControllerAdvice
public class CommonExceptionHandler {

    private final ExceptionBuilderService exceptionService;

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> refreshTokenExceptionHandler(
            RefreshTokenException refreshTokenException,
            WebRequest webRequest
            ){
        return exceptionService.build(HttpStatus.FORBIDDEN, refreshTokenException, webRequest);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(
            NotFoundException notFoundException,
            WebRequest webRequest
    ){
        return exceptionService.build(HttpStatus.NOT_FOUND, notFoundException, webRequest);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(
            BadRequestException badRequestException,
            WebRequest webRequest
    ){
        return exceptionService.build(HttpStatus.BAD_REQUEST, badRequestException, webRequest);
    }

}
