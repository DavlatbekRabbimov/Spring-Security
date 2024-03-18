package uz.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.security.service.SecurityService;
import uz.security.web.requests.AuthRequest;
import uz.security.web.requests.CreateUserRequest;
import uz.security.web.requests.RefreshTokenRequest;
import uz.security.web.responses.AuthResponse;
import uz.security.web.responses.CreateUserResponse;
import uz.security.web.responses.RefreshTokenResponse;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api")
public class PostController {

    private final SecurityService securityService;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authentication(@RequestBody AuthRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(securityService.authResponse(request));
    }

    @PostMapping("/creator")
    public ResponseEntity<CreateUserResponse> creation(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(securityService.createUserResponse(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(securityService.refreshTokenResponse(request));
    }

}
