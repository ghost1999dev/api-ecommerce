package com.example.demo.controllers;

import com.example.demo.dto.user.CreateUserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.repositories.UserRepository;
import com.example.demo.dto.user.LoginRequest;
import com.example.demo.dto.user.LoginResponse;
import com.example.demo.services.UserService;
import com.example.demo.utils.JwtUtil;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "valid", false,
                    "message", "Token no proporcionado",
                    "statusCode", HttpStatus.UNAUTHORIZED.value()));
        }
        String token = authHeader.substring(7);

        try {
            String email = jwtUtil.extractUsername(token);
            boolean userExists = userRepository.existsByEmail(email);
            if (!userExists || jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "valid", false,
                        "message", "Token invalido o expirado",
                        "statusCode", HttpStatus.UNAUTHORIZED.value()));
            }

            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "message", "Token valido",
                    "statusCode", HttpStatus.OK.value()));
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "valid", false,
                    "expired", true,
                    "message", "El token ha expirado",
                    "statusCode", HttpStatus.UNAUTHORIZED.value()));
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "valid", false,
                    "expired", false,
                    "message", "Token Invalido",
                    "statusCode", HttpStatus.UNAUTHORIZED.value()));

        }

    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request) {
        try {
            LoginResponse user = userService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "Message", e.getMessage(),
                    "statusCode", HttpStatus.BAD_REQUEST.value()));
        }

    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {

            LoginResponse loginResponse = userService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.UNAUTHORIZED.value()));
        }
    }

}
