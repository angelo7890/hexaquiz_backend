package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RequestUpdateProfileImageDto(
        @NotBlank(message = "profileUser nao pode ser em branco")
        @NotEmpty(message = "ProfileUser nao pode ser vazio")
        String profileImage) {
}
