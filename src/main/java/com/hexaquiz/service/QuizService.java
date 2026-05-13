package com.hexaquiz.service;

import com.hexaquiz.dto.log.DailyLogDto;
import com.hexaquiz.dto.log.LogDto;
import com.hexaquiz.dto.option.OptionDailyDto;
import com.hexaquiz.dto.question.QuestionDto;
import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.dto.request.RequestAnswerDto;
import com.hexaquiz.dto.request.RequestCreateGameSessionDto;
import com.hexaquiz.dto.response.*;
import com.hexaquiz.dto.session.SessionDto;
import com.hexaquiz.exception.error.ErrorException;
import com.hexaquiz.mapper.RankingMapper;
import com.hexaquiz.model.QuestionModel;
import com.hexaquiz.repository.GameSessionRepository;
import com.hexaquiz.repository.QuestionRepository;
import com.hexaquiz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

        int quizzesPlayed = userRepository.countByUserId(UUID.fromString(id));
        Integer pontos = userRepository.sumPointsByUserId(UUID.fromString(id));


        int totalPoints = pontos != null ? pontos : 0;

        int totalPossiblePoints = quizzesPlayed * 100;

        double accuracy = totalPossiblePoints == 0
                ? 0
                : ((double) totalPoints / totalPossiblePoints) * 10000;

        return new ResponseStatisticsDto(quizzesPlayed, accuracy, totalPoints);
    }

    public ResponseRankingsDto getRanking(String id, int page, int size, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Long geralRankingPosition = gameSessionRepository.getUserRankingPosition(UUID.fromString(id));
        if(geralRankingPosition == null){
            geralRankingPosition = 0L;
        }
        Page<RankingDto> geralRanking = gameSessionRepository.getGeneralRanking(pageable);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate
                .plusDays(1)
                .atStartOfDay();
        Long weeklyRankingPosition = gameSessionRepository.getWeeklyUserRankingPosition(UUID.fromString(id),start,end);
        if(weeklyRankingPosition == null){
            weeklyRankingPosition = 0L;
        }
        Page<RankingDto> weeklyRanking = gameSessionRepository.getWeeklyRanking(start, end, pageable);
        return rankingMapper.toResponsePaginationRankingDto(geralRanking,weeklyRanking, geralRankingPosition, weeklyRankingPosition);
    }

    @Transactional
    public ResponseAnswerDto answerQuestion(RequestAnswerDto request, String userId) {

        var session = gameSessionRepository
                .findByUserIdAndFinishedFalse(UUID.fromString(userId))
                .orElseThrow(() -> new ErrorException("Sem sessao ativa para esse id: "+ userId, HttpStatus.BAD_REQUEST));

        LocalDate today = getTime().toLocalDate();
        List<QuestionModel> questions =
                questionRepository.findByScheduledDateOrderBySequenceAsc(today);

        QuestionModel currentQuestion = questions.get(session.getGameSessionIndex());

        if (!currentQuestion.getId().equals(UUID.fromString(request.questionId()))) {
            throw new ErrorException("id das questoes sao diferentes", HttpStatus.BAD_REQUEST);
        }

        boolean correct = normalize(currentQuestion.getAnswer()).equals(normalize(request.answer()));
        if(correct){
            session.setPoints(session.getPoints()+ currentQuestion.getBasePoints());
        }
        int nextIndex = session.getGameSessionIndex() + 1;

        if (nextIndex >= questions.size()) {
            session.setFinished(true);
            session.setCompletedAt(getTime());
        } else {
            session.setGameSessionIndex(nextIndex);
        }

        gameSessionRepository.save(session);

        return new ResponseAnswerDto(
                correct,
                currentQuestion.getAnswer()
        );
    }
    public ResponseDailyQuestionsDto getDailyQuestions(String userId) {

        LocalDate today = getTime().toLocalDate();

        List<QuestionModel> questions =
                questionRepository.findByScheduledDateOrderBySequenceAsc(today);

        if (questions.isEmpty()) {
            throw new ErrorException("Sem questoes disponiveis para esse dia: "+today, HttpStatus.OK);
        }

        String quizId = questions.getFirst().getQuizId();

        var session = gameSessionRepository
                .findByUserIdAndQuizId(UUID.fromString(userId), quizId)
                .orElseGet(() -> {
                            gameSessionRepository.findByUserIdAndFinishedFalse(UUID.fromString(userId)).
                                    ifPresent(oldSession -> {
                                        oldSession.setFinished(true);
                                        oldSession.setCompletedAt(getTime());
                                        gameSessionRepository.save(oldSession);
                                    });
                            return gameSessionService.createGameSession(userId,new RequestCreateGameSessionDto(0,quizId));
                });

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

    public ResponseDailyLogDto getDailyLogs(LocalDate startDate) {
        if(startDate == null) {
            throw new ErrorException("data nao pode ser nula ou vazia",  HttpStatus.BAD_REQUEST);
        }
        LocalDate endDate = getTime().toLocalDate();
        List<DailyLogDto> response = new ArrayList<>();

        while (!startDate.isAfter(endDate)) {

            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = startDate.plusDays(1).atStartOfDay();

            List<LogDto> users =
                    gameSessionRepository.findUsersByDate(start, end);

            response.add(new DailyLogDto(startDate, users));

            startDate = startDate.plusDays(1);
        }

        return new ResponseDailyLogDto(response);
    }

    private QuestionDto mapQuestion(QuestionModel q) {

        String answer;

        switch (q.getType().getCode()) {

            case 1, 2, 5 -> {
                answer = "HIDDEN";
            }

            case 3, 4 -> {
                answer = Base64.getEncoder()
                        .encodeToString(q.getAnswer().getBytes(StandardCharsets.UTF_8));
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

    private String normalize(String value){
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase();
    }

    private LocalDateTime getTime(){
        return LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }
}
