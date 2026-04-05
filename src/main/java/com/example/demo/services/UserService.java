package com.example.demo.services;

import com.example.demo.dto.user.CreateUserRequest;
import com.example.demo.dto.user.CreateUserResponse;
import com.example.demo.dto.user.LoginRequest;
import com.example.demo.dto.user.LoginResponse;
import com.example.demo.dto.user.role.RoleDTO;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserHasRoles;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserHasRolesRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserHasRolesRepository userHasRolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Transactional
    public CreateUserResponse create(CreateUserRequest request){
        if (userRepository.existsByEmail(request.email)){
            throw new RuntimeException("El correo ya esta registrado");
        }
        User user = new User();
        user.setName(request.name);
        user.setLastname(request.lastname);
        user.setPhone(request.phone);
        user.setEmail(request.email);
        String encryptedPassword = passwordEncoder.encode(request.password);
        user.setPassword(encryptedPassword);
        User savedUser= userRepository.save(user);
        Role clientRole= roleRepository.findById("CLIENT").orElseThrow(
                ()-> new RuntimeException("El rol del cliente no existe")
        );
        UserHasRoles userHasRoles = new UserHasRoles(savedUser,clientRole);
        userHasRolesRepository.save(userHasRoles);
        CreateUserResponse createUserResponse=new CreateUserResponse();
        createUserResponse.setId(savedUser.getId());
        createUserResponse.setName(savedUser.getName());
        createUserResponse.setLastName(savedUser.getLastname());
        createUserResponse.setImage(savedUser.getImage());
        createUserResponse.setPhone(savedUser.getPhone());
        createUserResponse.setEmail(savedUser.getEmail());
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(savedUser.getId());
        List<RoleDTO> roleDTOS= roles.stream().map(
                role-> new RoleDTO(role.getId(),role.getName(),role.getImage(),role.getRoute())
        ).toList();
        createUserResponse.setRoles(roleDTOS);
        return createUserResponse;

    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("El email o Password no son validos"));
        System.out.println("ID del usuario encontrado: " + user.getId());
        System.out.println("Nombre: " + user.getName());
        System.out.println("Datos del Request: " + loginRequest.toString());
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new RuntimeException("El email o password no son validos");
        }
        String token = jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());
        List<RoleDTO> roleDTOS= roles.stream().map(
                role-> new RoleDTO(role.getId(),role.getName(),role.getImage(),role.getRoute())
        ).toList();
        CreateUserResponse createUserResponse=new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setName(user.getName());
        createUserResponse.setLastName(user.getLastname());
        createUserResponse.setImage(user.getImage());
        createUserResponse.setPhone(user.getPhone());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setRoles(roleDTOS);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("Bearer " + token);
        loginResponse.setCreateUserResponse(createUserResponse);

        return loginResponse;

    }
}
