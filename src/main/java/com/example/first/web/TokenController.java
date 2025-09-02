package com.example.first.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.first.dto.LoginRequest;
import com.example.first.dto.LoginResponse;

import com.example.first.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        java.util.Optional<com.example.first.entity.User> user = userRepository.findByUsername(loginRequest.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid!");
        }

        Instant now = Instant.now();
        long expiresIn = 300L;

        String scopes = user.get().getAccessGroups().stream()
                .flatMap(accessGroup -> accessGroup.getPermissions().stream())
                .flatMap(agp -> {
                    java.util.Set<String> featurePermissions = new java.util.HashSet<>();
                    String featureName = agp.getFeature().getName().toUpperCase().replace(" ", "_");
                    if (agp.isCanView()) featurePermissions.add(featureName + "_VIEW");
                    if (agp.isCanCreate()) featurePermissions.add(featureName + "_CREATE");
                    if (agp.isCanEdit()) featurePermissions.add(featureName + "_EDIT");
                    if (agp.isCanDelete()) featurePermissions.add(featureName + "_DELETE");
                    return featurePermissions.stream();
                })
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
