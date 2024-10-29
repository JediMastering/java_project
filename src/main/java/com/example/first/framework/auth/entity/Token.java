package com.example.first.framework.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "tb_tokens")
@Getter
@Setter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", nullable = false, length = 512)
    private String accessToken;

    @Column(name = "access_token_expiration", nullable = false)
    private LocalDateTime accessTokenExpiration;

    @Column(name = "refresh_token_expiration", nullable = false)
    private LocalDateTime refreshTokenExpiration;

    @Column(name = "is_active", nullable = true)
    private Integer isActive;

    // Relacionamento com o usuário (opcional, caso tenha uma entidade User)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
