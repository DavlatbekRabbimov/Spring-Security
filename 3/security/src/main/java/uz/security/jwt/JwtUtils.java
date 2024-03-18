package uz.security.jwt;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uz.security.exception.message.CustomException;

import java.io.Serializable;
import java.util.Date;

@Slf4j
@AllArgsConstructor
@Component
public class JwtUtils implements Serializable {

    private final String secretKey = "secretKey";
    private final CustomException exception;

    public String generateJwt(UserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid signature: {} ", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid token (structure jwt): {} ", e.getMessage());
        } catch (ExpiredJwtException e){
            log.error("Token is expired: {} ", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token is unsupported: {} ", e.getMessage());
        } catch (IllegalArgumentException e){
            log.error("Claims string is empty: {} ", e.getMessage());
        }
        return false;
    }

    private String generateTokenFromUsername(String username) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        } catch (Exception e) {
            throw exception.errorCatch("Error: generateTokenFromUsername is failed! - JwtUtils: ", e);
        }

    }

}
