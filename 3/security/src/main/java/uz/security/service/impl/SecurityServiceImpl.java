package uz.security.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.security.details.UserDetailsImpl;
import uz.security.exception.RefreshTokenException;
import uz.security.exception.message.CustomException;
import uz.security.jwt.JwtUtils;
import uz.security.model.entity.RefreshToken;
import uz.security.model.entity.User;
import uz.security.model.repo.UserRepo;
import uz.security.service.RefreshTokenService;
import uz.security.service.SecurityService;
import uz.security.web.requests.CreateUserRequest;
import uz.security.web.requests.AuthRequest;
import uz.security.web.requests.RefreshTokenRequest;
import uz.security.web.responses.AuthResponse;
import uz.security.web.responses.CreateUserResponse;
import uz.security.web.responses.RefreshTokenResponse;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authManager;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final JwtUtils jwtUtils;
    private final CustomException exception;

    @Override
    public AuthResponse authResponse(AuthRequest authRequest) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword =
                    new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );

            Authentication auth = authManager.authenticate(usernamePassword);
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return AuthResponse.builder()
                    .id(userDetails.getId())
                    .token(jwtUtils.generateJwt(userDetails))
                    .refreshToken(refreshToken.getToken())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .roles(roles)
                    .build();
        }catch (Exception e){
            throw exception.errorCatch("AuthResponse is failed! - SecurityServiceImpl", e);
        }
    }

    @Override
    public CreateUserResponse createUserResponse(CreateUserRequest createUserRequest) {

        CreateUserResponse response = new CreateUserResponse();
        if (userRepo.existsByUsername(createUserRequest.getUsername())) {
            response.setMessage("Warning: Username is already created!");
            return response;
        }
        if (userRepo.existsByEmail(createUserRequest.getEmail())) {
            response.setMessage("Warning: Email is already created!");
            return response;
        }
        try{
            User newUser = User.builder()
                    .username(createUserRequest.getUsername())
                    .email(createUserRequest.getEmail())
                    .password(passwordEncoder.encode(createUserRequest.getPassword()))
                    .roles(createUserRequest.getRoles())
                    .build();

            newUser.setRoles(createUserRequest.getRoles());
            userRepo.save(newUser);
            response.setMessage("Success: User is created!");
            return response;

        } catch (Exception e){
            throw exception.errorCatch("User creation is failed! " +
                    "- CreateUserResponse " +
                    "- SecurityServiceImpl", e);
        }
    }

    @Override
    public RefreshTokenResponse refreshTokenResponse(RefreshTokenRequest refreshTokenRequest) {

            String requestRefreshToken = refreshTokenRequest.getRefreshToken();

            if (requestRefreshToken == null) {
                throw exception.error("Refresh token is null!");
            }

            Optional<RefreshToken> optRefreshToken =
                    refreshTokenService.getRefreshToken(requestRefreshToken);

            if (optRefreshToken.isPresent()) {
                return optRefreshToken
                        .map(refreshTokenService::checkRefreshToken)
                        .map(RefreshToken::getUserId)
                        .map(userId -> {
                            User user = userRepo.findById(userId)
                                    .orElseThrow(() -> new RuntimeException("Error: User is not found!"));
                            UserDetails userDetails = new UserDetailsImpl(user);
                            String accessToken = jwtUtils.generateJwt(userDetails);
                            String refreshToken = refreshTokenService.createRefreshToken(userId).getToken();
                            return new RefreshTokenResponse(accessToken, refreshToken);
                        })
                        .orElseThrow(() -> new RefreshTokenException(
                                requestRefreshToken,
                                "Error: refresh token is not found!")
                        );
            } else {
                throw exception.error("RefreshTokenResponse is failed - SecurityServiceImpl");
            }
    }

}
