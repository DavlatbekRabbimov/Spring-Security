package uz.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.security.details.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl detailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest servletRequest,
            @NonNull HttpServletResponse servletResponse,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = this.parseJwt(servletRequest);
            if (jwt != null && jwtUtils.validate(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                UserDetails principalDetails = detailsService.loadUserByUsername(username);
                if (principalDetails != null) {
                    UsernamePasswordAuthenticationToken loginAuthToken =
                            new UsernamePasswordAuthenticationToken(
                                    principalDetails,
                                    null,
                                    principalDetails.getAuthorities()
                            );

                    WebAuthenticationDetails webAuthDetails =
                            new WebAuthenticationDetailsSource().buildDetails(servletRequest);

                    loginAuthToken.setDetails(webAuthDetails);

                    SecurityContextHolder.getContext().setAuthentication(loginAuthToken);
                }
            }

        } catch (Exception e) {
            log.error("Cannot set user authentication! - " + e.getMessage());
            servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String parseJwt(HttpServletRequest servletRequest) {
        String headerAuth = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
