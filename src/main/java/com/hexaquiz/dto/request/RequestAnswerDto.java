package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestAnswerDto(

        @NotBlank
        @NotEmpty
        UUID questionId,

        @NotBlank
        @NotEmpty
        String answer,

        @NotNull
        int attempts
) {
}
