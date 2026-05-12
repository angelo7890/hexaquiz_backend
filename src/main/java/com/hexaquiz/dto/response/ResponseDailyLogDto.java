package com.hexaquiz.dto.response;

import com.hexaquiz.dto.log.DailyLogDto;

import java.util.List;

public record ResponseDailyLogDto(
        List<DailyLogDto> log
) {
}
