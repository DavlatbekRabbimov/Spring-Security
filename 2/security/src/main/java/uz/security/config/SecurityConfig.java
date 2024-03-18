package uz.security.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;


import java.security.SecureRandom;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authManagerBuilder;
    private static final byte[] SALT = getSalt();

    public SecurityConfig(
            UserDetailsService userDetailsService,
            @Lazy PasswordEncoder passwordEncoder,
            AuthenticationManagerBuilder authManagerBuilder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authManagerBuilder = authManagerBuilder;
        configureGlobal();
    }

    public void configureGlobal() {
        try {
            authManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        } catch (Exception e) {
            log.error("Error: configuring AuthenticationManagerBuilder");
            throw new RuntimeException("Error configuring AuthenticationManagerBuilder", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        try {
            return new BCryptPasswordEncoder(10, new SecureRandom(SALT));
        } catch (Exception e) {
            log.error("Error: PasswordEncoder is not worked!");
            throw new RuntimeException("PasswordEncoder is not worked!", e);
        }
    }

    private static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                    .authorizeHttpRequests(api -> api
                            .requestMatchers("/public/**").permitAll()
                            .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated()

                    )
                    .csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(Customizer.withDefaults())
                    .sessionManagement(
                            httpSecuritySessionManagementConfigurer ->
                                    httpSecuritySessionManagementConfigurer
                                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling((exception) -> exception
                            .authenticationEntryPoint((request, response, authException) -> {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("Error: USER is not authorized!");
                            })
                            .accessDeniedHandler((request, response, accessDeniedException) -> {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("Error: USER could not get access!");
                            })
                    );
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException("Error: SecurityFilterChain is not worked!", e);
        }
    }
}
