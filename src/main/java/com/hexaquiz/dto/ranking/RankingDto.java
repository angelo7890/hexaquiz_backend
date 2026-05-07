package com.hexaquiz.dto.ranking;

import java.util.UUID;

public record RankingDto(
        UUID id,
        String username,
        String name,
        String profileImage,
        Long points
) {
}
