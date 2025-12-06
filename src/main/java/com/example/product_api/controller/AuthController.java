package com.example.product_api.controller;

import com.example.product_api.dto.LoginDTO;
import com.example.product_api.dto.RefreshTokenDTO;
import com.example.product_api.service.AuthService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação com JWT
 * - Login: retorna access token + refresh token
 * - Refresh: renova o access token usando refresh token
 * - Logout: revoga o refresh token
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${app.jwt.expiration-ms}")
    private Long accessTokenExpiration;

    /**
     * Login - Gera access token e refresh token
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        // Valida credenciais
        boolean authenticated = authService.validateCredentials(
            loginDTO.getUsername(), 
            loginDTO.getPassword()
        );

        if (!authenticated) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        // Gera access token e refresh token
        String[] tokens = authService.generateTokens(loginDTO.getUsername());
        String accessToken = tokens[0];

        // Monta resposta simples
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login efetuado com sucesso");
        response.put("username", loginDTO.getUsername());
        response.put("token", accessToken);

        return ResponseEntity.ok(response);
    }

    /**
     * Refresh Token - Renova o access token
     * POST /auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            // Gera novo access token
            String newAccessToken = authService.refreshAccessToken(refreshTokenDTO.getRefreshToken());

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("tokenType", "Bearer");
            response.put("expiresIn", accessTokenExpiration / 1000);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }

    /**
     * Logout - Revoga o refresh token
     * POST /auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String username) {
        authService.revokeRefreshToken(username);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout efetuado com sucesso");
        
        return ResponseEntity.ok(response);
    }
}
