package security.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import security.service.WebClientService;

@AllArgsConstructor
@RestController
@RequestMapping("api")
public class WebClientController {

    private final WebClientService webClientService;

    @GetMapping("/client")
    public Mono<String> getResponse(){
        return webClientService.sendRequest();
    }

}
