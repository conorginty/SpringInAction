package tacos.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tacos.User;
import tacos.UserRepository;

@Service
@Slf4j
public class UserDetailsServiceUsingRepo {
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                log.info("User {} successfully logged in", username);
                return user;
            }
            log.info("User {} failed to log in - username not recognised", username);
            throw new UsernameNotFoundException("User: " + username + " not found.");
        };
    }
}
