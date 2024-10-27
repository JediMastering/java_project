package com.example.first.framework.auth.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
