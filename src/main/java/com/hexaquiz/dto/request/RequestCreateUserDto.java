package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RequestCreateUserDto(
        @NotEmpty
        @NotBlank
        String name,

        @NotEmpty
        @NotBlank
        String username,

        @NotEmpty
        @NotBlank
        String email,

        @NotEmpty
        @NotBlank
        String password,

        @NotEmpty
        @NotBlank
        String profileUser
) {
}
