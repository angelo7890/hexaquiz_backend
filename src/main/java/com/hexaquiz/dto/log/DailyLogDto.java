package com.hexaquiz.dto.log;

import java.time.LocalDate;
import java.util.List;

public record DailyLogDto(
        LocalDate date,
        List<LogDto> users
) {
}
