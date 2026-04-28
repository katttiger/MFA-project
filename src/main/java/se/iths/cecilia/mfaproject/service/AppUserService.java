package se.iths.cecilia.mfaproject.service;

import lombok.RequiredArgsConstructor;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.iths.cecilia.mfaproject.model.AppUser;
import se.iths.cecilia.mfaproject.repository.AppUserRepository;


@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(AppUser appUser) {
        if (appUser.isAllowsMFA()) {
            String secret = Base32.random();
            appUser.setMfaSecret(secret);
        }
        String passwordEncrypted = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(passwordEncrypted);

        appUserRepository.save(appUser);
    }


}
