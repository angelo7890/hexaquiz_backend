package com.hexaquiz.service;

import com.hexaquiz.dto.request.*;
import com.hexaquiz.dto.response.ResponseLoginDto;
import com.hexaquiz.dto.response.ResponsePaginationUserDto;
import com.hexaquiz.dto.response.ResponseUserDto;
import com.hexaquiz.exception.error.ErrorException;
import com.hexaquiz.mapper.UserMapper;
import com.hexaquiz.model.UserModel;
import com.hexaquiz.repository.UserRepository;
import com.hexaquiz.security.service.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = new UserMapper();
    public UserService(UserRepository userRepository, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseLoginDto createUser(RequestCreateUserDto dto) {
         if(dto.password().length() < 6) {
             throw new ErrorException("Password deve conter no minimo 6 caracteres", HttpStatus.BAD_REQUEST );
         }
         if(userRepository.existsByusername(dto.username())){
             throw new ErrorException("Username ja existe", HttpStatus.BAD_REQUEST);
         }
         if(userRepository.existsByemail(dto.email())){
             throw new ErrorException("Email ja existe", HttpStatus.BAD_REQUEST);
         }

         userRepository.save(new UserModel(dto.name(), dto.username(), passwordEncoder.encode(dto.password()), dto.profileUser(), dto.email()));

         return authService.login(new RequestLoginDto(dto.username(), dto.password()));
    }

    public ResponseUserDto getUserById(String id) {
       var user =  userRepository.findByid(UUID.fromString(id));
        return userMapper.toDto(user);
    }

    public ResponsePaginationUserDto getAllUsers(int page , int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<UserModel> users = userRepository.findAll(pageable);
        return userMapper.toPaginationDto(users);
    }

    public ResponseUserDto updateProfileImage(String id, RequestUpdateProfileImageDto dto) {
        var user = userRepository.findByid(UUID.fromString(id));
        user.setProfileImage(dto.profileImage());
        userRepository.save(user);
        return  userMapper.toDto(user);
    }
    public void updatePassword(String id, RequestUpdatePasswordDto dto) {
        if(dto.password().length() < 6) {
            throw new ErrorException("password deve conter no minimo 6 caracteres", HttpStatus.BAD_REQUEST );
        }
        var user = userRepository.findByid(UUID.fromString(id));
        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    public ResponseUserDto updateNameAndUsername(RequestUpdateNameAndUsernameDto dto, String id){
        var user  = userRepository.findByid(UUID.fromString(id));
        if(user == null){
            throw new ErrorException("usuario nao encontrado", HttpStatus.BAD_REQUEST);
        }
        if(dto.username()!= null){
            if(userRepository.existsByusername(dto.username())){
                throw new ErrorException("Username ja existe", HttpStatus.BAD_REQUEST);
            }
            user.setUsername(dto.username());
        }
        if(dto.name()!= null){
            user.setName(dto.name());
        }
        userRepository.save(user);
        return  userMapper.toDto(user);
    }

    public void deleteUser(String id) {
        var user = userRepository.findByid(UUID.fromString(id));
        userRepository.delete(user);
    }
}
