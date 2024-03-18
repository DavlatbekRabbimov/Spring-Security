package uz.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class UserController {

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getUser(@AuthenticationPrincipal UserDetails details) {
        return getUserDetails(details);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE')")
    public ResponseEntity<String> getAdmin(@AuthenticationPrincipal UserDetails details) {
        return getUserDetails(details);
    }

    private ResponseEntity<String> getUserDetails(UserDetails details) {
        try {
            String username = details.getUsername();
            String userAuth = details.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            String response =
                    String.format("Username: ".concat(username) + " - " + "User role: ".concat(userAuth));

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}