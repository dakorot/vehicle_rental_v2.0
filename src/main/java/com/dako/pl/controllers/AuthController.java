package com.dako.pl.controllers;

import com.dako.pl.entities.User;
import com.dako.pl.repository.IUserRepository;
import com.dako.pl.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(IUserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.getUser(request.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found!");
        }

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is incorrect!");
        }

        String token = jwtService.generateToken(user.getLogin(), user.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
    }
}
