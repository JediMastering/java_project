package com.example.first.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    private String nome;

    @NotBlank(message =  "Login cannot be empty")
    private String login;
}
