package uz.security.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import uz.security.exception.message.ErrorResponse;
import uz.security.service.ExceptionBuilderService;

@Service
public class ExceptionBuilderServiceImpl implements ExceptionBuilderService {
    @Override
    public ResponseEntity<ErrorResponse> build(
            HttpStatus httpStatus,
            Exception e,
            WebRequest request) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponse.builder()
                        .exceptionMessage(e.getMessage())
                        .description(request.getDescription(false))
                        .build()
                );
    }
}
