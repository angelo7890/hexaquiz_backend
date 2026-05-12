package com.hexaquiz.dto.log;

import java.util.UUID;

public record LogDto(
        UUID id,
        String username,
        String name,
        String profileUser
) {
}
