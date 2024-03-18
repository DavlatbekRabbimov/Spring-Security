package uz.security.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import uz.security.exception.message.CustomException;
import uz.security.exception.message.InfoMessage;
import uz.security.model.entity.RefreshToken;

@Slf4j
@AllArgsConstructor
@Component
public class RedisExpirationEvent {

    private final CustomException exception;
    private final InfoMessage message;

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event){

        RefreshToken expiredRefreshToken = (RefreshToken) event.getValue();

        if (expiredRefreshToken == null) {
            throw exception.notFoundErrorLogic("Expired refresh token is not found!");
        }

        long id = expiredRefreshToken.getId();
        String token = expiredRefreshToken.getToken();
        message.info("Expired refresh token message: id: {}, token: {}", id, token);
    }
}
