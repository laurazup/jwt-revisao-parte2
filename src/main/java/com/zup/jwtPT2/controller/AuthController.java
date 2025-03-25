package com.zup.jwtPT2.controller;

import com.zup.jwtPT2.dto.LoginRequest;
import com.zup.jwtPT2.dto.RegisterRequest;
import com.zup.jwtPT2.dto.TokenResponse;
import com.zup.jwtPT2.model.User;
import com.zup.jwtPT2.service.UserService;
import com.zup.jwtPT2.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {

        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles()); // Adiciona as roles como claim

        String token = jwtUtil.generateToken(user.getUsername(), claims);

        return new TokenResponse(token, "refreshToken");
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestHeader("Authorization") String refreshToken) {
        // Remove o prefixo "Bearer " do token, se existir
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        // Valida o token de refresh
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Token de refresh inválido ou expirado");
        }

        // Extrai o username do token de refresh
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        // Cria claims personalizados
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", "ROLE_USER"); // Exemplo de claim
        claims.put("username", username);

        // Gera um novo token de acesso
        String newAccessToken = jwtUtil.generateToken(username, claims);

        // Retorna o novo token de acesso e mantém o mesmo token de refresh
        return new TokenResponse(newAccessToken, refreshToken);
    }

    @PostMapping("/register")
    public TokenResponse register(@RequestBody RegisterRequest registerRequest) {
        // Define roles padrão para novos usuários
        Set<String> roles = Set.of("ROLE_USER");

        // Registra o usuário
        User user = userService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), roles);

        // Cria claims personalizados
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles()); // Adiciona roles como claim

        // Gera o token JWT
        String token = jwtUtil.generateToken(user.getUsername(), claims);

        return new TokenResponse(token, "refreshToken");
    }

    // Endpoint para listar usuários - apenas ADMIN pode acessar
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Endpoint para promover um usuário para ADMIN
    @PutMapping("/promote/{username}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode acessar
    public String promoteToAdmin(@PathVariable String username) {
        userService.promoteToAdmin(username);
        return "Usuário " + username + " promovido a ADMIN com sucesso!";
    }
}