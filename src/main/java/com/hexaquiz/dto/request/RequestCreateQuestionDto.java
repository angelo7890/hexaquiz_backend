package com.hexaquiz.dto.request;

import com.hexaquiz.dto.option.OptionDto;
import com.hexaquiz.enums.QuestionTypeEnum;

import java.time.LocalDate;
import java.util.List;

public record RequestCreateQuestionDto(
        String text,
        QuestionTypeEnum type,
        String answer,
        int basePoints,
        String image,
        LocalDate scheduledDate,
        int sequence,
        String quizId,
        List<OptionDto> options
){
}
