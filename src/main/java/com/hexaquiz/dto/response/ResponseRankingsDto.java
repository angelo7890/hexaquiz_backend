package com.hexaquiz.dto.response;

import com.hexaquiz.dto.ranking.PaginationRankingDto;

public record ResponseRankingsDto(
        PaginationRankingDto geralRanking,
        PaginationRankingDto weeklyRanking
) {
}
