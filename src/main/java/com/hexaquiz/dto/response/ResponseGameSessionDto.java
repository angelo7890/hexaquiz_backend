package com.hexaquiz.dto.response;

import java.time.LocalDateTime;

public record ResponseGameSessionDto(
         String id,

         String userId,

         int gameSessionIndex,

         int points,

         boolean finished,

         LocalDateTime createdAt,

         LocalDateTime completedAt
) {
}
