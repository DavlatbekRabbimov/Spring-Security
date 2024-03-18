package security.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import security.provider.WebClientProvider;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Configuration
public class WebClientConfig {

    @Bean
    public WebClientProvider webClientProvider() {
        return () -> WebClient.create("http://localhost:8282");
    }

    @Bean
    public Mono<String> authAndGetToken(WebClientProvider webClientProvider) {

        WebClient webClient = webClientProvider.getWebClient();

        Map<String, String> authBody = new HashMap<>();
        authBody.put("username", "client");
        authBody.put("password", "secretKey");

        return webClient.post()
                .uri("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authBody)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if (response != null && response.get("token") != null) {
                        return Mono.just((String) response.get("token"));
                    } else {
                        log.error("Error: Token is null");
                        return Mono.error(new RuntimeException("Token is null"));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error: While getting Token", e);
                    return Mono.empty();
                });
    }
}
