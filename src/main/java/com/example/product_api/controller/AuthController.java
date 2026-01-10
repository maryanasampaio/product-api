package com.example.product_api.controller;

import com.example.product_api.dto.LoginDTO.LoginRequestDTO;
import com.example.product_api.dto.LoginDTO.LoginResponseDTO;
import com.example.product_api.dto.TokenDTO.RefreshTokenRequestDTO;
import com.example.product_api.dto.TokenDTO.TokenResponseDTO;
import com.example.product_api.service.AuthService;

import jakarta.validation.Valid;

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
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) {

         boolean authenticated = authService.validateCredentials(
            loginDTO.getUsername(), 
            loginDTO.getPassword()
        );

        if (!authenticated) {
            LoginResponseDTO error = new LoginResponseDTO(
                "Credenciais inválidas",
                loginDTO.getUsername(),
                null,
                null
            );
            return ResponseEntity.status(401).body(error);
        }

        // Gera access token e refresh token (refresh token é salvo no banco)
        String[] tokens = authService.generateTokens(loginDTO.getUsername());
        String accessToken = tokens[0];

        String permission = authService.findByUsername(loginDTO.getUsername())
                .map(u -> u.getPermission())
                .orElse("USER");
        LoginResponseDTO response = new LoginResponseDTO(
            "Login efetuado com sucesso",
            loginDTO.getUsername(),
            accessToken,
            permission
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Refresh Token - Renova o access token
     * POST /auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenDTO) {
        try {
            // Gera novo access token
            String newAccessToken = authService.refreshAccessToken(refreshTokenDTO.getRefreshToken());

            TokenResponseDTO response = new TokenResponseDTO(
                newAccessToken,
                refreshTokenDTO.getRefreshToken(),
                accessTokenExpiration / 1000
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                "message", "Refresh token inválido ou expirado"
            ));
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
