package com.hexaquiz.service;

import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.dto.request.RequestCreateGameSessionDto;
import com.hexaquiz.dto.response.ResponseGameSessionDto;
import com.hexaquiz.dto.response.ResponsePaginationGameSessionDto;
import com.hexaquiz.dto.response.ResponsePaginationRankingDto;
import com.hexaquiz.mapper.GameSessionMapper;
import com.hexaquiz.mapper.RankingMapper;
import com.hexaquiz.model.GameSessionModel;
import com.hexaquiz.model.UserModel;
import com.hexaquiz.repository.GameSessionRepository;
import com.hexaquiz.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public GameSessionModel createGameSession(String userId, RequestCreateGameSessionDto dto) {

        UUID uuid = UUID.fromString(userId);

        UserModel user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean hasActive = gameSessionRepository
                .existsByUserIdAndFinishedFalse(uuid);

        if (hasActive) {
            throw new RuntimeException("User already has an active session");
        }
        GameSessionModel session = new GameSessionModel(dto.gameSessionIndex(), dto.quizId());
        session.setUser(user);
        return gameSessionRepository.save(session);
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

    public void DeleteGameSession(String GameSessionId){
        gameSessionRepository.delete(gameSessionRepository.findByid(UUID.fromString(GameSessionId)));
    }

}
