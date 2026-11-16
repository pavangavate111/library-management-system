package com.library.lms.controller;

import com.library.lms.dto.AuthResponse;
import com.library.lms.dto.LoginRequest;
import com.library.lms.dto.RegisterRequest;
import com.library.lms.dto.ForgotPasswordRequest;
import com.library.lms.dto.ResetPasswordRequest;

import com.library.lms.security.AuthenticationService;
import com.library.lms.service.PasswordResetService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;

    public AuthController(
            AuthenticationService authenticationService,
            PasswordResetService passwordResetService) {

        this.authenticationService = authenticationService;
        this.passwordResetService = passwordResetService;
    }

    // Register API
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response = authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authenticationService.login(request);

        return ResponseEntity.ok(response);
    }

    // Send password reset link
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        passwordResetService.sendResetLink(request.email());

        return ResponseEntity.ok(
                "If the email is registered, a password reset link will be sent."
        );
    }

    // Reset password using reset link token
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(
                request.token(),
                request.newPassword()
        );

        return ResponseEntity.ok("Password reset successfully");
    }
}