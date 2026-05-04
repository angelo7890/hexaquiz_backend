package com.hexaquiz.dto.response;

import com.hexaquiz.dto.question.QuestionDto;
import com.hexaquiz.dto.session.SessionDto;

import java.util.List;

public record ResponseDailyQuestionsDto(

        List<QuestionDto> questions,
        SessionDto session
) {
}
