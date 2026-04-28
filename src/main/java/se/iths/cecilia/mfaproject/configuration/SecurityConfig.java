package se.iths.cecilia.mfaproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import se.iths.cecilia.mfaproject.model.AppUser;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                            AppUser user = (AppUser) authentication.getPrincipal();
                            assert user != null;
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
