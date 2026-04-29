package se.iths.cecilia.mfaproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.iths.cecilia.mfaproject.service.MFAService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mfa")
public class MFAController {

    private final MFAService mfaService;

    @GetMapping
    public String getVerificationPage(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "mfa";
    }

    @GetMapping(value = "/qr", produces = "image/png")
    @ResponseBody
    public byte[] getQrCode(@RequestParam String username) throws Exception {
        BufferedImage image = mfaService.generateQrCodeImage(username);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam String code, Principal principal, Model model) {
        boolean isValid = mfaService.verifyCode(principal.getName(), Integer.parseInt(code));
        if (isValid) {
            return "redirect:/home";
        } else {
            model.addAttribute("error", true);
            return "mfa";
        }
    }
}
