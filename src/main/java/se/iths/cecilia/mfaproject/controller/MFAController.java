package se.iths.cecilia.mfaproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mfa")
public class MFAController {

    @GetMapping("/qr")
    public String getQRPage() {
        return "qr";
    }

    @GetMapping
    public String getVerificationPage() {
        return "verify-2fa";
    }

}
