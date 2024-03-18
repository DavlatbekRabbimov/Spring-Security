package uz.security.details;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.security.model.entity.User;
import uz.security.model.repo.UserRepo;

import java.text.MessageFormat;

@AllArgsConstructor
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepo.findByUsername(username);
            return new UserDetailsImpl(user);
        } catch (UsernameNotFoundException e) {
            String error = MessageFormat.format("Username: {} is not found", username);
            throw new UsernameNotFoundException(error);
        }
    }
}
