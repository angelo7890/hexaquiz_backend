package com.hexaquiz.dto.ranking;

import java.util.List;

public record PaginationRankingDto(
        List<RankingDto> content,
        long positionRanking,
        int totalPages,
        long totalElements,
        int pageSize,
        int currentPages
) {
}
