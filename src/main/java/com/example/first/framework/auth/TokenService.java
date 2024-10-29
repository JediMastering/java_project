package com.example.first.framework.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.first.framework.auth.entity.Role;
import com.example.first.framework.auth.entity.Token;
import com.example.first.framework.auth.entity.User;
import com.example.first.framework.auth.repository.TokenRepository;

@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    public String getGeneratedJwt(User user, Long expiresIn) {
        Instant now = Instant.now();

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public void saveNewToken(User user, String jwtValue, Long expiresIn, Long refreshToken) {
        Token token = new Token();
        token.setUser(user);
        token.setAccessToken(jwtValue);
        token.setAccessTokenExpiration(LocalDateTime.now().plusSeconds(expiresIn));
        token.setRefreshTokenExpiration(LocalDateTime.now().plusSeconds(refreshToken));
        token.setIsActive(1);
        tokenRepository.save(token);
    }

    public void setTokenAsInactive(Token token){
        token.setIsActive(null);
        tokenRepository.save(token);
    }
}
