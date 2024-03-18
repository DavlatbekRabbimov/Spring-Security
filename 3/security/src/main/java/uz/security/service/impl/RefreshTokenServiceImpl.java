package uz.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.security.exception.message.CustomException;
import uz.security.model.entity.RefreshToken;
import uz.security.model.repo.RefreshTokenRepo;
import uz.security.service.RefreshTokenService;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${security.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;
    private final RefreshTokenRepo refreshTokenRepo;
    private final CustomException exception;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepo refreshTokenRepo, CustomException exception) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.exception = exception;
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(String token){
        Optional<RefreshToken> refreshToken = refreshTokenRepo.findByToken(token);
        if (refreshToken.isPresent()) {
            return refreshToken;
        }
        else {
            throw exception.error("Error: Refresh Token is not gotten! " +
                            "- getRefreshToken " +
                            "- RefreshTokenServiceImpl");
        }
    }

    @Override
    public RefreshToken createRefreshToken(Long userId){
        try{
            RefreshToken refreshToken = RefreshToken.builder()
                    .userId(userId)
                    .expireDate(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
                    .token(UUID.randomUUID().toString())
                    .build();
            refreshToken = refreshTokenRepo.save(refreshToken);
            return refreshToken;
        }catch (Exception e){
            throw exception.errorCatch(
                    "Error: Refresh Token is not created! " +
                            "- createRefreshToken " +
                            "- RefreshTokenServiceImpl ", e);
        }
    }

    @Override
    public RefreshToken checkRefreshToken(RefreshToken token){
        if (token.getExpireDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(token);
            throw exception.error("Token is expired! " +
                    "- checkRefreshToken " +
                    "- RefreshTokenServiceImpl");
        }
        return token;
    }


}
