package se.iths.cecilia.mfaproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.cecilia.mfaproject.service.AppUserService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
    private AppUserService appUserService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "registration";
    }
}
