package com.example.demo.services;

import com.example.demo.dto.user.*;
import com.example.demo.dto.user.role.RoleDTO;
import com.example.demo.dto.user.user.mapper.UserMapper;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    @Autowired
    private UserMapper userMapper;
    @Transactional
    public LoginResponse create(CreateUserRequest request){
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
        UserResponse userResponse =new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setName(savedUser.getName());
        userResponse.setLastName(savedUser.getLastname());
        userResponse.setImage(savedUser.getImage());
        userResponse.setPhone(savedUser.getPhone());
        userResponse.setEmail(savedUser.getEmail());
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(savedUser.getId());
        List<RoleDTO> roleDTOS= roles.stream().map(
                role-> new RoleDTO(role.getId(),role.getName(),role.getImage(),role.getRoute())
        ).toList();
        userResponse.setRoles(roleDTOS);
        LoginResponse loginResponse = new LoginResponse();
        String token = jwtUtil.generateToken(user);
        loginResponse.setToken("Bearer " + token);
        loginResponse.setUserResponse(userResponse);
        return loginResponse;

    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("El email o Password no son validos"));
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new RuntimeException("El email o password no son validos");
        }
        String token = jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());
        List<RoleDTO> roleDTOS= roles.stream().map(
                role-> new RoleDTO(role.getId(),role.getName(),role.getImage(),role.getRoute())
        ).toList();
        UserResponse userResponse =new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastname());
        userResponse.setImage(user.getImage());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoles(roleDTOS);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("Bearer " + token);
        loginResponse.setUserResponse(userResponse);
        return loginResponse;
    }

    //Find user by id
    @Transactional
    public UserResponse findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("El email no existe para este usuario"));
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());
        List<RoleDTO> roleDTOS= roles.stream().map(
                role-> new RoleDTO(role.getId(),role.getName(),role.getImage(),role.getRoute())
        ).toList();
        UserResponse userResponse =new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastname());
        userResponse.setImage(user.getImage());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoles(roleDTOS);
        return userResponse;

    }
    //Find user by id
    @Transactional
    public UserResponse updateUserWithImage(Long id, UpdateUserRequest request) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("El email o Password no son validos"));
        if(request.getName() != null){
            user.setName(request.getName());
        }
        if(request.getLastname()!=null){
            user.setLastname(request.getLastname());
        }

        if(request.getPhone()!= null){
            user.setPhone(request.getPhone());
        }
        if(request.getFile() != null && !request.getFile().isEmpty()){
            String uploadDir = "uploads/users/" + user.getId();
            String filename = request.getFile().getOriginalFilename();
            String filePath = Paths.get(uploadDir,filename).toString();
            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(request.getFile().getInputStream(),Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            user.setImage("/" + filePath.replace("\\","/"));

        }
        userRepository.save(user);
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());

        return userMapper.toUserResponse(user,roles);

    }
}
