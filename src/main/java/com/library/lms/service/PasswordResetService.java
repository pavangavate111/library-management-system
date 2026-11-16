package com.library.lms.service;

import com.library.lms.entity.User;
import com.library.lms.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, ResetData> resetTokens =
            new ConcurrentHashMap<>();

    private static final long TOKEN_EXPIRY_SECONDS = 900;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public PasswordResetService(
            UserRepository userRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // Generate and send password reset link
    public void sendResetLink(String email) {

        String normalizedEmail = email.trim().toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElse(null);

        // Do not reveal whether email exists
        if (user == null) {
            return;
        }

        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        ResetData data = new ResetData(
                normalizedEmail,
                Instant.now().plusSeconds(TOKEN_EXPIRY_SECONDS)
        );

        resetTokens.put(token, data);

        String resetLink = frontendUrl
                + "/reset-password?token="
                + token;

        emailService.sendPasswordResetEmail(
                normalizedEmail,
                resetLink
        );
    }

    // Reset password using email link token
    public void resetPassword(String token, String newPassword) {

        ResetData data = resetTokens.remove(token);

        if (data == null) {
            throw new RuntimeException(
                    "Invalid or already used password reset link"
            );
        }

        if (Instant.now().isAfter(data.expiryTime)) {
            throw new RuntimeException(
                    "Password reset link has expired"
            );
        }

        User user = userRepository.findByEmail(data.email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid password reset request"
                        )
                );

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    private static class ResetData {

        private final String email;
        private final Instant expiryTime;

        private ResetData(String email, Instant expiryTime) {
            this.email = email;
            this.expiryTime = expiryTime;
        }
    }
}