package uz.security.exception.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InfoMessage {
    public void info(String message, Object o1, Object o2){
        log.info(message, o1, o2);
    }

}
