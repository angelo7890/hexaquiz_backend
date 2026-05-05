package com.hexaquiz.dto.response;

public record ResponseAnswerDto(
        boolean correct,
        String correctAnswerPayload
) {
}
