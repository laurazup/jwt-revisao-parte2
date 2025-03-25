package com.zup.jwtPT2.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @ElementCollection
    private Set<String> roles; // Roles do usuário (ex.: ROLE_USER, ROLE_ADMIN)
}