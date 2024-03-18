package uz.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.security.dto.UserDto;
import uz.security.model.entity.Role;
import uz.security.model.entity.User;
import uz.security.model.repo.UserRepo;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(String username, String password, Role role) {
        try {
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            User user = new User(username, passwordEncoder.encode(password), roles);
            userRepo.save(user);
            return UserDto.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roleType(role.getType())
                    .build();
        } catch (Exception e){
            throw new RuntimeException("Error: User is not created! - user service", e);
        }
    }
}

