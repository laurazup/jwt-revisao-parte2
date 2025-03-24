package com.zup.jwtPT2.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}