package com.hexaquiz.dto.request;

import jakarta.validation.constraints.NotNull;

public record RequestCreateGameSessionDto(
        @NotNull
        int gameSessionIndex,
        @NotNull
        int points
) {
}
