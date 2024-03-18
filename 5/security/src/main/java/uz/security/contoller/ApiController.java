package uz.security.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import uz.security.register.Registration;

@RestController
@RequestMapping("api")
public class ApiController {

    @GetMapping("/public")
    public Mono<ResponseEntity<String>> getPublicPage(){
        return Mono.just(ResponseEntity.ok("OK: Public page is opened!"));
    }

    @PostMapping("/public/register")
    public Mono<ResponseEntity<String>> createUser(@RequestBody Registration user){
        return Mono.just(ResponseEntity.ok("OK: User - email: " + user.getEmail()));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ADMIN')")
    @GetMapping("/user")
    public Mono<ResponseEntity<String>> getUserPage(){
        return Mono.just(ResponseEntity.ok("OK: User page is opened!"));
    }



}
