package uz.security.web.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private long id;

    private String token;

    private String refreshToken;

    private String username;

    private String email;

    private List<String> roles;

}
