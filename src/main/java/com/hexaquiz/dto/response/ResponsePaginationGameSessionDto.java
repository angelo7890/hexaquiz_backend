package com.hexaquiz.dto.response;

import java.util.List;

public record ResponsePaginationGameSessionDto(
        List<ResponseGameSessionDto> content,
        int totalPages,
        long totalElements,
        int pageSize,
        int currentPages
) {
}
