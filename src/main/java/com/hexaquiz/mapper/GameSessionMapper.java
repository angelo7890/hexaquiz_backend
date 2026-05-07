package com.hexaquiz.mapper;

import com.hexaquiz.dto.response.ResponsePaginationGameSessionDto;
import com.hexaquiz.dto.response.ResponseGameSessionDto;
import com.hexaquiz.model.GameSessionModel;
import org.springframework.data.domain.Page;

import java.util.List;

public class GameSessionMapper {

    public ResponseGameSessionDto toDto(GameSessionModel gameSessionModel){
        return new ResponseGameSessionDto(
                gameSessionModel.getId().toString(),
                gameSessionModel.getUser().getId().toString(),
                gameSessionModel.getGameSessionIndex(),
                gameSessionModel.getPoints(),
                gameSessionModel.isFinished(),
                gameSessionModel.getCreatedAt(),
                gameSessionModel.getCompletedAt());
    }

    public ResponsePaginationGameSessionDto toPaginationDto(Page<GameSessionModel> games) {
        List<ResponseGameSessionDto> content = games.stream().map(
                gameSessionModel -> new ResponseGameSessionDto(
                        gameSessionModel.getId().toString(),
                        gameSessionModel.getUser().getId().toString(),
                        gameSessionModel.getGameSessionIndex(),
                        gameSessionModel.getPoints(),
                        gameSessionModel.isFinished(),
                        gameSessionModel.getCreatedAt(),
                        gameSessionModel.getCompletedAt())
        ).toList();
        return new ResponsePaginationGameSessionDto(content, games.getTotalPages(), games.getTotalElements(), games.getSize(), games.getNumber());
    }
}
