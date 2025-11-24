package com.example.product_api.controller;

import com.example.product_api.dto.LoginDTO;
import com.example.product_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        boolean authenticated = authService.login(loginDTO.getUsername(), loginDTO.getPassword());

        if (!authenticated) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        return ResponseEntity.ok("Login efetuado com sucesso");
    }
}
