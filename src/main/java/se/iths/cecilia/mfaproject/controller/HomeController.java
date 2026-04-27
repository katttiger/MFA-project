package se.iths.cecilia.mfaproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String getHomePage(HttpSession session) {
        Boolean verified = (Boolean) session.getAttribute("MFA_VERIFIED");
        if (verified == null || !verified) {
            return "redirect:/verify-2fa";
        }
        return "home";
    }
}
