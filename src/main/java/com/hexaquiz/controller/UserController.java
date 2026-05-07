package com.hexaquiz.controller;

import com.hexaquiz.dto.request.RequestCreateUserDto;
import com.hexaquiz.dto.request.RequestLoginDto;
import com.hexaquiz.dto.request.RequestUpdatePasswordDto;
import com.hexaquiz.dto.request.RequestUpdateProfileImageDto;
import com.hexaquiz.dto.response.ResponseLoginDto;
import com.hexaquiz.dto.response.ResponsePaginationUserDto;
import com.hexaquiz.dto.response.ResponseUserDto;
import com.hexaquiz.security.service.AuthService;
import com.hexaquiz.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/hexaquiz/user")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseUserDto> create(@Valid @RequestBody RequestCreateUserDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDto> login(@Valid @RequestBody RequestLoginDto request){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getUser(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<ResponsePaginationUserDto>  findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PatchMapping("/update/image/{id}")
    public ResponseEntity<ResponseUserDto> updateImage(@PathVariable String id, @RequestBody RequestUpdateProfileImageDto dto){
        return ResponseEntity.ok(userService.updateProfileImage(id, dto));
    }

    @PatchMapping("/update/password/{id}")
    public ResponseEntity<?>  updatePassword(@PathVariable String id, @RequestBody RequestUpdatePasswordDto dto){
        userService.updatePassword(id, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
