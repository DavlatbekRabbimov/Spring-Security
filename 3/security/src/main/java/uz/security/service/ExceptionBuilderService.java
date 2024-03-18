package uz.security.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import uz.security.exception.message.ErrorResponse;


public interface ExceptionBuilderService {

    ResponseEntity<ErrorResponse> build(HttpStatus status, Exception e, WebRequest request);

}
