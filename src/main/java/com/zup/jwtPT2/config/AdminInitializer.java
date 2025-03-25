package com.zup.jwtPT2.config;

import com.zup.jwtPT2.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:adminPassword}")
    private String adminPassword;

    public AdminInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userService.isUsernameTaken(adminUsername)) {
            userService.registerUser(adminUsername, adminPassword, Set.of("ROLE_ADMIN")); // Role correta
            System.out.println("Usuário ADMIN inicial criado com sucesso!");
        }
    }
}