package com.hexaquiz.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RequestCreateUserDto(
        @NotBlank(message = "name nao pode ser em branco")
        @NotEmpty(message = "name nao pode ser vazio")
        String name,

        @NotBlank(message = "username nao pode ser em branco")
        @NotEmpty(message = "username nao pode ser vazio")
        String username,

        @NotBlank(message = "email nao pode ser em branco")
        @NotEmpty(message = "email nao pode ser vazio")
        @Email(message = "digite um email valido")
        String email,

        @NotBlank(message = "password nao pode ser em branco")
        @NotEmpty(message = "password nao pode ser vazio")
        @Size(min = 6, message = "password deve conter no minimo 6 caracteres")
        String password,

        @NotBlank(message = "profileUser nao pode ser em branco")
        @NotEmpty(message = "ProfileUser nao pode ser vazio")
        String profileUser
) {
}
