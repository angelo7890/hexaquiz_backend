package com.hexaquiz.dto.response;

import com.hexaquiz.dto.ranking.RankingDto;

import java.util.List;

public record ResponsePaginationRankingDto(
        List<RankingDto> content,
        int totalPages,
        long totalElements,
        int pageSize,
        int currentPages
) {
}
