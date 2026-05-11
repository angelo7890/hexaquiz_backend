package com.hexaquiz.dto.response;

public record ResponseStatisticsDto(
        int quizzesPlayed,
        double accuracy,
        int points

) {
}
