package tacos.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity // enables global method security (like @PreAuthorize etc)
public class SecurityConfig {

    // We’ll use this bean both when creating new users and when authenticating users at login
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Applies bcrypt strong hashing encryption
    }

    /* Here we define 2 users as part of the security configuration. We can now login as wither of these users
    The in-memory user details service is convenient for testing purposes or for very simple applications, but it
    doesn't allow for easy editing of users. If you need to add, remove, or change a user, you’ll have to make the
    necessary changes and then rebuild and redeploy the application. */
    /* had to comment out the below bean, as it interfered with our other userDetailsService bean */
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
//        List<UserDetails> usersList = new ArrayList<>();
//        usersList.add(new User("buzz", encoder.encode("password"),
//            List.of(new SimpleGrantedAuthority("ROLE_USER"))));
//        usersList.add(new User("woody", encoder.encode("password"),
//            List.of(new SimpleGrantedAuthority("ROLE_USER"))));
//
//        return new InMemoryUserDetailsManager(usersList);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Needed for the moment to create new taco resources with POST.
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/design", "/orders").hasRole("USER")
                .anyRequest().permitAll()
                /* To replace the built-in login page custom login page. */
            ).formLogin(form -> form
                .loginPage("/login")
            )
            /* By default, a successful login will take the user directly to the page that they were navigating to when
            Spring Security determined that they needed to log in. If the user were to directly navigate to the login page,
            a successful login would take them to the root path (for example, the home page) */
            .oauth2Login(form ->
                form.loginPage("/login")
            /* Below sets up a security filter that intercepts POST requests to /logout. Therefore, to provide logout
            capability, you just need to add a logout form and button to the views in your application */
            ).logout(logoutConfigurer ->
                logoutConfigurer.logoutUrl("/logout")
            );
        return http.build();
    }
    /* The order of the rules in the filterChain() method is important. Security rules declared first take precedence
    over those declared lower down. If you were to swap the order of those two security rules, all requests would
    have permitAll() applied to them; the rule for /design and /orders requests would have no effect */
}
