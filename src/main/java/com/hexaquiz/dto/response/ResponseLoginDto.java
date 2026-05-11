package com.hexaquiz.dto.response;

import com.hexaquiz.dto.tokens.Tokens;

public record ResponseLoginDto(
        Tokens tokens,
        ResponseUserDto user,

        long positionRanking
) {
}
