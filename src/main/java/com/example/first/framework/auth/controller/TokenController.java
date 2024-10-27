package com.example.first.framework.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.first.framework.auth.TokenService;
import com.example.first.framework.auth.dto.LoginRequest;
import com.example.first.framework.auth.dto.LoginResponse;
import com.example.first.framework.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
public class TokenController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var user = userRepository.findByUsername(loginRequest.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        Long expiresIn = 300L;
        Long refreshToken = 3000L;

        String jwtValue = tokenService.getGeneratedJwt(user.get(),expiresIn);
        String uuidString = tokenService.getGeneratedRefreshToken(refreshToken);
        tokenService.SaveNewToken(user.get(), jwtValue, expiresIn, uuidString, refreshToken);

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
