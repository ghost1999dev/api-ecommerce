package com.example.demo.dto.user.user.mapper;

import com.example.demo.config.ApiConfig;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.dto.user.role.RoleDTO;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserMapper {

    public UserResponse toUserResponse(User user, List<Role> roles){

        List<RoleDTO> roleDTOS= roles.stream().map(
                role-> new RoleDTO(role.getId(),role.getName(),role.getImage(),role.getRoute())
        ).toList();
        UserResponse userResponse =new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastname());

        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoles(roleDTOS);

        if (user.getImage() != null){
            String imageUrl= ApiConfig.BASE_URL +user.getImage();
            userResponse.setImage(imageUrl);
        }
        return userResponse;
    }
}
