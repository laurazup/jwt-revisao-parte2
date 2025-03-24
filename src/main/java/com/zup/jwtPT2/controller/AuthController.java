package com.zup.jwtPT2.controller;

import com.zup.jwtPT2.dto.LoginRequest;
import com.zup.jwtPT2.dto.TokenResponse;
import com.zup.jwtPT2.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        // Simulação de autenticação
        if ("user".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());
            return new TokenResponse(token, "refreshToken");
        }
        throw new RuntimeException("Credenciais inválidas");
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestHeader("Authorization") String refreshToken) {
        // Simulação de renovação
        String newToken = jwtUtil.generateToken("user");
        return new TokenResponse(newToken, "newRefreshToken");
    }
}