package uz.security.exception.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.security.exception.NotFoundException;

@Slf4j
@Component
public class CustomException {
    public RuntimeException notFoundErrorLogic(String message) {
        log.error(message);
        throw new NotFoundException(message);
    }

    public RuntimeException error(String message) {
        log.error(message);
        throw new RuntimeException(message);
    }

    public RuntimeException errorCatch(String message, Exception e) {
        log.error(message);
        throw new RuntimeException(message, e);
    }


}
