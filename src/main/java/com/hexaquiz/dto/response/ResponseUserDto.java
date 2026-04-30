package com.hexaquiz.dto.response;

import com.hexaquiz.enums.UserTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ResponseUserDto(
        String id,
        String name,
        String username,
        String profileUser,
        LocalDateTime createdAt,
        UserTypeEnum type
) {
}
