package se.iths.cecilia.mfaproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import se.iths.cecilia.mfaproject.model.AppUser;
import se.iths.cecilia.mfaproject.repository.AppUserRepository;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final AppUserRepository appUserRepository;

    public SecurityConfig(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetails() {
        return username -> {
            AppUser appUser = appUserRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(("User with username " + username + " was not found")));
            return new CustomUserDetails(appUser);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/home", "/mfa").authenticated()
                .anyRequest().permitAll()
        );
        http.formLogin(login -> login
                .successHandler(
                        (request, response, authentication) ->
                        {
                            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

                            if (user.isAllowsMFA()) {
                                response.sendRedirect("/mfa");
                            } else {
                                response.sendRedirect("/home");
                            }
                        })
        );
        http.logout(Customizer.withDefaults());
        return http.build();
    }


}
