package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RequestLoginDto(
        @NotBlank(message = "username nao pode ser em branco")
        @NotEmpty(message = "username nao pode ser vazio")
        String username,

        @NotBlank(message = "password nao pode ser em branco")
        @NotEmpty(message = "password nao pode ser vazio")
        String password
) {
}
