package se.iths.cecilia.mfaproject.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Service
public class MFAService {
    private final AppUserService appUserService;

    public BufferedImage generateQrCodeImage(String username) throws Exception {
        String issuer = "mfaproject".trim();
        String secret = appUserService.getUser(username).getMfaSecret();
        username = URLEncoder.encode(username.replace("+", "%20"));

        String otAuthUrl = "otpauth://totp/" + issuer + ":" + username +
                "?secret=" + secret +
                "&issuer=" + issuer;

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(otAuthUrl, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public boolean verifyCode(String username, int code) {
        String secret = appUserService.getUser(username).getMfaSecret().trim();
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

        System.out.println("SECRET: " + secret);
        System.out.println("CODE: " + code);
        return googleAuthenticator.authorize(secret, code);
    }

}
