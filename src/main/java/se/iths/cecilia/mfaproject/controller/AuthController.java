package se.iths.cecilia.mfaproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.iths.cecilia.mfaproject.model.AppUser;
import se.iths.cecilia.mfaproject.service.AppUserService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {
    private final AppUserService appUserService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new AppUser());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute AppUser user) {
        appUserService.addUser(user);
        return "redirect:/";
    }
}
