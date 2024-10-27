package com.example.first.framework.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.first.framework.auth.TokenService;
import com.example.first.framework.auth.entity.Token;
import com.example.first.framework.auth.repository.TokenRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // private final JwtService jwtService;
    // private final UserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extrai o token do cabeçalho Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Remove "Bearer " para pegar o token puro
        Optional<Token> token = tokenRepository.findByAccessTokenAndIsActive(jwt,1);

        if(token.isEmpty()){
            throw new BadCredentialsException("Token is not active!");
        }
        
        //AJUSTAR A LÓGICA A PARTIR DESTE PONTO
        LocalDateTime now = LocalDateTime.now();

        if(token.get().getRefreshTokenExpiration().isBefore(now)){
            throw new BadCredentialsException("Token is not active!");
        }

        if(token.get().getAccessTokenExpiration().isBefore(now)){
            Long expiresIn = 300L;
            Long refreshToken = 3000L;
    
            String jwtValue = tokenService.getGeneratedJwt(token.get().getUser(),expiresIn);
            String uuidString = tokenService.getGeneratedRefreshToken(refreshToken);
            tokenService.SaveNewToken(token.get().getUser(), jwtValue, expiresIn, uuidString, refreshToken);
        }

        filterChain.doFilter(request, response);
    }
}

