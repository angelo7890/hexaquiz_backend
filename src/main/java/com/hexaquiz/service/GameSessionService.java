package com.hexaquiz.service;

import com.hexaquiz.dto.request.RequestCreateGameSessionDto;
import com.hexaquiz.dto.response.ResponseGameSessionDto;
import com.hexaquiz.dto.response.ResponsePaginationGameSessionDto;
import com.hexaquiz.mapper.GameSessionMapper;
import com.hexaquiz.model.GameSessionModel;
import com.hexaquiz.repository.GameSessionRepository;
import com.hexaquiz.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final GameSessionMapper gameSessionMapper =  new GameSessionMapper();
    public GameSessionService(GameSessionRepository gameSessionRepository, UserRepository userRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
    }

    public void createGameSession(String userId, RequestCreateGameSessionDto dto){
       var user = userRepository.findByid(UUID.fromString(userId));
       if(user == null){
           throw new RuntimeException("User not found");
       }
       gameSessionRepository.save(new GameSessionModel(user, dto.gameSessionIndex(), dto.points()));
    }

    public ResponseGameSessionDto getGameSession(String GameSessionId){
        var gameSessionModel = gameSessionRepository.findByid(UUID.fromString(GameSessionId));
        return gameSessionMapper.toDto(gameSessionModel);
    }

    public ResponsePaginationGameSessionDto getAllGameSessions(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<GameSessionModel> gameSessions = gameSessionRepository.findAll(pageable);
        return gameSessionMapper.toPaginationDto(gameSessions);
    }

    public void updateFinished(String GameSessionId){
        var gameSessionModel = gameSessionRepository.findByid(UUID.fromString(GameSessionId));
        gameSessionModel.setFinished(true);
        gameSessionModel.setCompletedAt(LocalDateTime.now());
        gameSessionRepository.save(gameSessionModel);
    }

    public void DeleteGameSession(String GameSessionId){
        gameSessionRepository.delete(gameSessionRepository.findByid(UUID.fromString(GameSessionId)));
    }


}
