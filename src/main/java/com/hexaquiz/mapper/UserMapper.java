package com.hexaquiz.mapper;

import com.hexaquiz.dto.response.ResponsePaginationUserDto;
import com.hexaquiz.dto.response.ResponseUserDto;
import com.hexaquiz.model.UserModel;
import org.springframework.data.domain.Page;

import java.util.List;

public class UserMapper {

    public ResponseUserDto toDto(UserModel user){
        return new ResponseUserDto(
                user.getId().toString(),
                user.getName(),
                user.getUsername(),
                user.getProfileImage(),
                user.getCreatedAt(),
                user.getType());
    }

    public ResponsePaginationUserDto toPaginationDto(Page<UserModel> users) {
        List<ResponseUserDto> content = users.stream().map(
                userModel -> new ResponseUserDto(
                        userModel.getId().toString(),
                        userModel.getName(),
                        userModel.getUsername(),
                        userModel.getProfileImage(),
                        userModel.getCreatedAt(),
                        userModel.getType())
        ).toList();
        return new ResponsePaginationUserDto(content, users.getTotalPages(), users.getTotalElements(), users.getSize(), users.getNumber());
    }

}
