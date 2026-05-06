package com.example.demo.dto.user;

import com.example.demo.dto.user.role.RoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class UserResponse {

    public Long id;
    public String name;
    public String lastName;
    public String email;
    public String phone;
    public String image;
    @JsonProperty("notification_token")
    public String notificationToken;

    List<RoleDTO> roles;
}
