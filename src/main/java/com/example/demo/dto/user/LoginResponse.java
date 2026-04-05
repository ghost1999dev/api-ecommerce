package com.example.demo.dto.user;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private CreateUserResponse createUserResponse;
}
