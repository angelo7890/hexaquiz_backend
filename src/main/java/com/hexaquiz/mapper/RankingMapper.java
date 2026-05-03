package com.hexaquiz.mapper;

import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.dto.response.ResponsePaginationRankingDto;
import org.springframework.data.domain.Page;

public class RankingMapper {
    public ResponsePaginationRankingDto toResponsePaginationRankingDto(Page<RankingDto> ranking){

        return new ResponsePaginationRankingDto(
                ranking.getContent(),
                ranking.getTotalPages(),
                ranking.getTotalElements(),
                ranking.getSize(),
                ranking.getNumber());
    }
}
