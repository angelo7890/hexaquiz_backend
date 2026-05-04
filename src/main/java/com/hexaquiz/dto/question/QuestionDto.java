package com.hexaquiz.dto.question;

import com.hexaquiz.dto.option.OptionDailyDto;
import com.hexaquiz.enums.QuestionTypeEnum;

import java.util.List;

public record QuestionDto(

        String id,
        String text,
        int type,
        String image,
        List<OptionDailyDto> options,
        String answer,
        int basePoints
) {
}
