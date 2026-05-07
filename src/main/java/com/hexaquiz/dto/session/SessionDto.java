package com.hexaquiz.dto.session;

public record SessionDto(
        int index,
        int points,
        boolean finished
) {
}
