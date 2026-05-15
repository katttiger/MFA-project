package se.iths.cecilia.mfaproject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    @PreAuthorize("isFullyAuthenticated()")
    public String getHomePage() {
        return "home";
    }
}
