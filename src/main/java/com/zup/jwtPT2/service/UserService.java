package com.zup.jwtPT2.service;

import com.zup.jwtPT2.model.User;
import com.zup.jwtPT2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário no sistema.
     *
     * @param username Nome de usuário.
     * @param password Senha do usuário.
     * @param roles    Conjunto de roles do usuário.
     * @return Usuário registrado.
     */
    public User registerUser(String username, String password, Set<String> roles) {
        // Verifica se o usuário já existe
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }

        // Cria um novo usuário com a senha criptografada
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);

        // Salva o usuário no banco de dados
        return userRepository.save(user);
    }

    /**
     * Busca um usuário pelo nome de usuário.
     *
     * @param username Nome de usuário.
     * @return Optional contendo o usuário, se encontrado.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}