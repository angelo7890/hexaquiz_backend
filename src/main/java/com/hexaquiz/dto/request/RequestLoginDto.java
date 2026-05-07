package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RequestLoginDto(
        @NotEmpty
        @NotBlank
        String username,

        @NotEmpty
        @NotBlank
        String password
) {
}
