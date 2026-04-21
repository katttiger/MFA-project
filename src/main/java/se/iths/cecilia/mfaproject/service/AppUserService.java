package se.iths.cecilia.mfaproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.iths.cecilia.mfaproject.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private AppUserRepository appUserRepository;


}
