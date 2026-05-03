package com.hexaquiz.dto.response;

public record ResponseAnswerDto(
        boolean correct,
        int pointsEarned,
        String correctAnswerPayload
) {
}
