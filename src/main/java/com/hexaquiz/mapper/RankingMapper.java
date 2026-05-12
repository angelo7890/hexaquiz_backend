package com.hexaquiz.mapper;

import com.hexaquiz.dto.ranking.PaginationRankingDto;
import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.dto.response.ResponseRankingsDto;
import org.springframework.data.domain.Page;

public class RankingMapper {
    public ResponseRankingsDto toResponsePaginationRankingDto(Page<RankingDto> geralRanking,Page<RankingDto> weeklyRanking,long geralPosition, long weeklyPosition){
        PaginationRankingDto geral = new PaginationRankingDto(
                geralRanking.getContent(),
                geralPosition,
                geralRanking.getTotalPages(),
                geralRanking.getTotalElements(),
                geralRanking.getSize(),
                geralRanking.getNumber());
        PaginationRankingDto weekly = new PaginationRankingDto(
                weeklyRanking.getContent(),
                weeklyPosition,
                weeklyRanking.getTotalPages(),
                weeklyRanking.getTotalElements(),
                weeklyRanking.getSize(),
                weeklyRanking.getNumber());

        return new ResponseRankingsDto(geral, weekly);
    }
}
