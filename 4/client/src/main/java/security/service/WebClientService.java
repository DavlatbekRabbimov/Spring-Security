package security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import security.provider.WebClientProvider;

@Slf4j
@Service
public class WebClientService {

    private final WebClientProvider webClientProvider;
    private final Mono<String> jwtToken;


    public WebClientService(WebClientProvider webClientProvider, Mono<String> jwtToken) {
        this.webClientProvider = webClientProvider;
        this.jwtToken = jwtToken;
    }

    public Mono<String> sendRequest() {
        return jwtToken.flatMap(token ->
                webClientProvider.getWebClient().get()
                        .uri("/api/user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                                Mono.error(new RuntimeException("Client Error: send request to user")))
                        .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                                Mono.error(new RuntimeException("Server Error: send request to user")))
                        .bodyToMono(String.class)
        ).doOnError(e -> {
            log.error("Error: client request is not sent to server");
        });
    }
}
