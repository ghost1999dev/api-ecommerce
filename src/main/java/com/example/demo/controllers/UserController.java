package com.example.demo.controllers;

import com.example.demo.dto.user.UserResponse;
import com.example.demo.dto.user.UpdateUserRequest;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try{
            UserResponse response = userService.findById(id);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "Message",e.getMessage(),
                "statusCode",HttpStatus.NOT_FOUND.value()
            ));
        }
    }
    @PutMapping(value = "/upload/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @ModelAttribute UpdateUserRequest request){
        try{
            UserResponse response = userService.updateUserWithImage(id,request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "Message",e.getMessage(),
                    "statusCode",HttpStatus.NOT_FOUND.value()
            ));
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "Message",e.getMessage(),
                    "statusCode",HttpStatus.NOT_FOUND.value()
            ));
        }
    }
}
