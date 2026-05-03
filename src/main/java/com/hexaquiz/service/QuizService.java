package com.hexaquiz.service;

import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.dto.request.RequestAdvancedDto;
import com.hexaquiz.dto.request.RequestAnswerDto;
import com.hexaquiz.dto.response.ResponseAnswerDto;
import com.hexaquiz.dto.response.ResponsePaginationRankingDto;
import com.hexaquiz.dto.response.ResponseStatisticsDto;
import com.hexaquiz.mapper.RankingMapper;
import com.hexaquiz.repository.GameSessionRepository;
import com.hexaquiz.repository.QuestionRepository;
import com.hexaquiz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QuizService {

    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final QuestionRepository questionRepository;
    private final RankingMapper rankingMapper =  new RankingMapper();

    public QuizService(UserRepository userRepository, GameSessionRepository gameSessionRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.questionRepository = questionRepository;
    }

    public ResponseStatisticsDto statisticsUser(String id){
        int quizzesPlayed = userRepository.countFinishedByUserId(UUID.fromString(id));
        int pontos = userRepository.sumPointsByUserId(UUID.fromString(id));
        int accuracy = quizzesPlayed / pontos * 100;
        return new ResponseStatisticsDto(quizzesPlayed, accuracy);
    }

    public ResponsePaginationRankingDto getRanking(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<RankingDto> ranking = gameSessionRepository.getGeneralRanking(pageable);
        return rankingMapper.toResponsePaginationRankingDto(ranking);
    }

    @Transactional
    public ResponseAnswerDto answerQuestion(RequestAnswerDto request, String userId) {

        var question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        boolean correct = question.getAnswer()
                .equalsIgnoreCase(request.answer());

        int pointsEarned = calculatePoints(
                correct,
                request.attempts(),
                question.getBasePoints()
        );

        var session = gameSessionRepository
                .findByUserIdAndFinishedFalse(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("No active session found"));

        if (correct) {
            session.setPoints(session.getPoints() + pointsEarned);
        }
        return new ResponseAnswerDto(
                correct,
                pointsEarned,
                question.getAnswer()
        );
    }

    @Transactional
    public void advanceQuiz(RequestAdvancedDto request, String userId) {

        var session = gameSessionRepository
                .findByUserIdAndFinishedFalse(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("No active session found"));

        session.setGameSessionIndex(request.newIndex());

        if (request.finished()) {
            session.setFinished(true);
            session.setCompletedAt(LocalDateTime.now());
        }

        gameSessionRepository.save(session);
    }

    private int calculatePoints(boolean correct, int attempts, int basePoints) {
        if (!correct) return 0;

        return switch (attempts) {
            case 1 -> basePoints;
            case 2 -> (int) (basePoints * 0.7);
            case 3 -> (int) (basePoints * 0.4);
            default -> 0;
        };
    }

}
