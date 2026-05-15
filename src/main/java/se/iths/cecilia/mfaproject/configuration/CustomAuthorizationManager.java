package se.iths.cecilia.mfaproject.configuration;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.*;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CustomAuthorizationManager implements AuthorizationManager {


    private final AuthorizationManager<Object> mfa = AllAuthoritiesAuthorizationManager
            .hasAllAuthorities(FactorGrantedAuthority.PASSWORD_AUTHORITY, FACTOR_TOTP);

    private final AuthorizationManager<Object> passwordOnly = AuthorityAuthorizationManager
            .hasAuthority(FactorGrantedAuthority.PASSWORD_AUTHORITY);

    public static final String FACTOR_TOTP = "FACTOR_TOTP";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public @Nullable AuthorizationResult authorize(Supplier authentication, Object context) {
        log.info("Authorixing {}", authentication.get());
        if (authentication.get() instanceof UsernamePasswordAuthenticationToken upat) {
            if (upat.getPrincipal() instanceof CustomUserDetails accountUserDetails) {
                CustomUserDetails details = (CustomUserDetails) upat.getPrincipal();
                if (details.isAllowsMFA()) {
                    return this.mfa.authorize(authentication, context);
                } else {
                    return this.passwordOnly.authorize(authentication, context);
                }
            }
        }
        log.warn("Authentication is not of expected type");

        return new AuthorizationDecision(false);
    }
}
