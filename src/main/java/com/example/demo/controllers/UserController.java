package com.example.demo.controllers;

import com.example.demo.dto.user.CreateUserRequest;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

   // @PostMapping
//    public ResponseEntity<User>create(@RequestBody CreateUserRequest request){
//        //User user = userService.create(request);
//        return ResponseEntity.ok(user);
//    }
}
