package com.hexaquiz.service;

import com.hexaquiz.dto.request.RequestCreateUserDto;
import com.hexaquiz.dto.response.ResponsePaginationUserDto;
import com.hexaquiz.dto.response.ResponseUserDto;
import com.hexaquiz.mapper.UserMapper;
import com.hexaquiz.model.UserModel;
import com.hexaquiz.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = new UserMapper();
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseUserDto createUser(RequestCreateUserDto dto) {
         if(dto.password().length() < 6) {
             throw new IllegalArgumentException("Password too short");
         }
         if(userRepository.existsByusername(dto.username())){
             throw new IllegalArgumentException("Username already exists");
         }
         if(userRepository.existsByemail(dto.email())){
             throw new IllegalArgumentException("Email already exists");
         }

         var user = userRepository.save(new UserModel(dto.name(), dto.username(), passwordEncoder.encode(dto.password()), dto.profileUser(), dto.email()));

         return userMapper.toDto(user);
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

    public void updateProfileImage(String id, String profileImage) {
        var user = userRepository.findByid(UUID.fromString(id));
        user.setProfileImage(profileImage);
        userRepository.save(user);

        //TODO: tenho que retornar o usuario com o avatar atualizado
    }
    public void updatePassword(String id, String password) {
        if(password.length() < 6) {
            throw new IllegalArgumentException("Password too short");
        }
        var user = userRepository.findByid(UUID.fromString(id));
        user.setPassword(password);
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        var user = userRepository.findByid(UUID.fromString(id));
        userRepository.delete(user);
    }
}
