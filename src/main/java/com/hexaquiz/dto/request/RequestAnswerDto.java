package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestAnswerDto(

        @NotBlank(message = "questionId nao pode ser em branco")
        @NotEmpty(message = "questionId nao pode ser vazio")
        String questionId,

        @NotBlank(message = "answer nao pode ser em branco")
        @NotEmpty(message = "answer nao pode ser vazio")
        String answer
) {
}
