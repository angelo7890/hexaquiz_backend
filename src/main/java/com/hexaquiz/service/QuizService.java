package com.hexaquiz.service;

import com.hexaquiz.dto.option.OptionDailyDto;
import com.hexaquiz.dto.question.QuestionDto;
import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.dto.request.RequestAdvancedDto;
import com.hexaquiz.dto.request.RequestAnswerDto;
import com.hexaquiz.dto.request.RequestCreateGameSessionDto;
import com.hexaquiz.dto.response.ResponseAnswerDto;
import com.hexaquiz.dto.response.ResponseDailyQuestionsDto;
import com.hexaquiz.dto.response.ResponsePaginationRankingDto;
import com.hexaquiz.dto.response.ResponseStatisticsDto;
import com.hexaquiz.dto.session.SessionDto;
import com.hexaquiz.mapper.RankingMapper;
import com.hexaquiz.model.QuestionModel;
import com.hexaquiz.repository.GameSessionRepository;
import com.hexaquiz.repository.QuestionRepository;
import com.hexaquiz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class QuizService {

    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final QuestionRepository questionRepository;
    private final GameSessionService gameSessionService;
    private final RankingMapper rankingMapper =  new RankingMapper();

    public QuizService(UserRepository userRepository, GameSessionRepository gameSessionRepository, QuestionRepository questionRepository, GameSessionService gameSessionService) {
        this.userRepository = userRepository;
        this.gameSessionRepository = gameSessionRepository;
        this.questionRepository = questionRepository;
        this.gameSessionService = gameSessionService;
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

    public ResponseDailyQuestionsDto getDailyQuestions(String userId) {
        LocalDate today = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        String quizId = generateDailyQuizId(today);

        List<QuestionModel> questions =
                questionRepository.findByScheduledDateOrderBySequenceAsc(today);

        var session = gameSessionRepository
                .findByUserIdAndQuizId(UUID.fromString(userId), quizId)
                .orElseGet(() -> gameSessionService.createGameSession(userId, new RequestCreateGameSessionDto(0, quizId)));

        List<QuestionDto> questionDTOs = questions.stream()
                .map(this::mapQuestion)
                .toList();

        var sessionDTO = new SessionDto(
                session.getGameSessionIndex(),
                session.getPoints(),
                session.isFinished()
        );

        return new ResponseDailyQuestionsDto(questionDTOs, sessionDTO);
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

    private String generateDailyQuizId(LocalDate date) {
        return "DAILY_" + date;
    }

    private QuestionDto mapQuestion(QuestionModel q) {

        String answer;

        switch (q.getType().getCode()) {

            case 1, 2, 5 -> {
                answer = "HIDDEN";
            }

            case 3, 4 -> {
                answer = Base64.getEncoder()
                        .encodeToString(q.getAnswer().getBytes());
            }

            default -> answer = "HIDDEN";
        }

        List<OptionDailyDto> options = q.getOptions() != null
                ? q.getOptions().stream()
                .map(opt -> new OptionDailyDto(
                        opt.getId().toString(),
                        opt.getText(),
                        opt.getImage()
                ))
                .toList()
                : List.of();

        return new QuestionDto(
                q.getId().toString(),
                q.getText(),
                q.getType().getCode(),
                q.getImage(),
                options,
                answer,
                q.getBasePoints()
        );
    }

}
