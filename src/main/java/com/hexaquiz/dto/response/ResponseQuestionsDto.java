package com.hexaquiz.dto.response;

import com.hexaquiz.dto.question.QuestionDto;

import java.util.List;

public record ResponseQuestionsDto(
        List<QuestionDto> questions
) {
}
