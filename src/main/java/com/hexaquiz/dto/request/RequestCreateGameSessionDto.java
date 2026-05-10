package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RequestCreateGameSessionDto(
        @NotNull(message = "gameSessionIndex nao pode ser nulo")
        int gameSessionIndex,
        @NotBlank(message = "quizId nao pode ser em branco")
        @NotEmpty(message = "quizId nao pode ser vazio")
        String quizId
) {
}
