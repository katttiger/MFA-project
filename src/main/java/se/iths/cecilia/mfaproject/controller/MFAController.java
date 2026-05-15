package se.iths.cecilia.mfaproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.iths.cecilia.mfaproject.configuration.CustomAuthorizationManager;
import se.iths.cecilia.mfaproject.service.MFAService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
            updateSecurityContextWithTotpFactor();
            return "redirect:/home";
        } else {
            model.addAttribute("error", true);
            return "mfa";
        }


    }

    private static void updateSecurityContextWithTotpFactor() {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        assert currentAuth != null;
        Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>(currentAuth.getAuthorities());

        updatedAuthorities.add(new SimpleGrantedAuthority(CustomAuthorizationManager.FACTOR_TOTP));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                Objects.requireNonNull(currentAuth.getPrincipal()),
                currentAuth.getCredentials(),
                updatedAuthorities
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}

