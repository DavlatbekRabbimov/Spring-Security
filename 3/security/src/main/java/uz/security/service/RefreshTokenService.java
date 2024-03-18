package uz.security.service;

import uz.security.model.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> getRefreshToken(String token);
    RefreshToken createRefreshToken(Long id);
    RefreshToken checkRefreshToken(RefreshToken token);
}
