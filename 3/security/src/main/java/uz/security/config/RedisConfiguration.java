package uz.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import uz.security.model.entity.RefreshToken;

import java.time.Duration;
import java.util.Collections;

@EnableRedisRepositories(
        keyspaceConfiguration = RedisConfiguration.RefreshTokenConfig.class,
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
@Configuration
public class RedisConfiguration extends KeyspaceConfiguration {

    @Value("${security.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;
    private final RedisProperties redisProperties;

    public RedisConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public JedisConnectionFactory jedisFactory(RedisProperties redisProperties) {
        return new JedisConnectionFactory(
                new RedisStandaloneConfiguration(
                        redisProperties.getHost(),
                        redisProperties.getPort()
                ));
    }

    @Bean(name = "redisKeyValueTemplate")
    public RedisKeyValueTemplate redisKeyValueTemplate() {
        RedisMappingContext mappingContext = new RedisMappingContext();
        RedisKeyValueAdapter keyValueAdapter = new RedisKeyValueAdapter(redisTemplate());
        return new RedisKeyValueTemplate(keyValueAdapter, mappingContext);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFactory(redisProperties));
        return template;
    }

    public class RefreshTokenConfig extends KeyspaceConfiguration {

        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(
                    RefreshToken.class,
                    "refresh_token");
            keyspaceSettings.setTimeToLive(refreshTokenExpiration.getSeconds());
            return Collections.singleton(keyspaceSettings);
        }
    }

}
