package com.application.erp.passwordreset.service;

import com.application.erp.shared.service.EmailService;
import com.application.erp.user.domain.entity.User;
import com.application.erp.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${password.reset.token.expiration-minutes}")
    private long expirationMinutes;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Autowired
    public PasswordResetService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void processForgotPassword(String userEmail) {
        userRepository.findByEmail(userEmail).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            user.setPasswordResetToken(token);
            user.setPasswordResetTokenExpiryDate(LocalDateTime.now().plusMinutes(expirationMinutes));
            userRepository.save(user);

            String resetLink = frontendBaseUrl + "/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        });
    }

    public void processResetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (user.getPasswordResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired");
        }

        logger.info("Received new password for reset: {}", newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        logger.info("Encoded password: {}", encodedPassword);
        user.setPassword(encodedPassword);
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiryDate(null);
        userRepository.save(user);
    }
}
