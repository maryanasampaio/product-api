package com.example.product_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "Usuário obrigatório")
    private String username;

    @NotBlank(message = "Senha obrigatória")
    private String password;

    @Data
    @AllArgsConstructor
    public static class LoginResponseDTO {
        private String accessToken;
        private String refreshToken;
        private String message;
    }
}
