package com.example.first.web;

import com.example.first.dto.ForgotPasswordRequestDto;
import com.example.first.dto.ResetPasswordRequestDto;
import com.example.first.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        passwordResetService.processForgotPassword(request.email());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Solicitação de redefinição de senha processada. Se o e-mail estiver cadastrado, um link será enviado.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        try {
            passwordResetService.processResetPassword(request.token(), request.newPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // In a real app, you'd have more specific exceptions and handlers
            return ResponseEntity.badRequest().build();
        }
    }
}
