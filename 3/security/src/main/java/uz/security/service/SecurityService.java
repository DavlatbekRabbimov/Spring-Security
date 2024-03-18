package uz.security.service;

import uz.security.web.requests.AuthRequest;
import uz.security.web.requests.CreateUserRequest;
import uz.security.web.requests.RefreshTokenRequest;
import uz.security.web.responses.AuthResponse;
import uz.security.web.responses.CreateUserResponse;
import uz.security.web.responses.RefreshTokenResponse;

public interface SecurityService {

    AuthResponse authResponse(AuthRequest authRequest);
    CreateUserResponse createUserResponse(CreateUserRequest createUserRequest);
    RefreshTokenResponse refreshTokenResponse(RefreshTokenRequest refreshTokenRequest);
}
