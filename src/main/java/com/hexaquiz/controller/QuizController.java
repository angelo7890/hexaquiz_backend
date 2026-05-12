package com.hexaquiz.controller;

import com.hexaquiz.dto.request.RequestAnswerDto;
import com.hexaquiz.dto.response.*;
import com.hexaquiz.service.QuizService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/hexaquiz/")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/answer/{id}")
    public ResponseEntity<ResponseAnswerDto> answer(@PathVariable String id, @RequestBody RequestAnswerDto requestAnswerDto){
        return ResponseEntity.ok(quizService.answerQuestion(requestAnswerDto, id));
    }
    @GetMapping("/log")
    public ResponseEntity<ResponseDailyLogDto> getDailyLogs(@RequestParam LocalDate date) {
        return ResponseEntity.ok(quizService.getDailyLogs(date));
    }

    @GetMapping("/statistics/{id}")
    public ResponseEntity<ResponseStatisticsDto> statistics(@PathVariable String id){
        return ResponseEntity.ok(quizService.statisticsUser(id));
    }

    @GetMapping("/ranking/{id}")
    public ResponseEntity<ResponseRankingsDto> ranking(@PathVariable String id,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam LocalDate startDate,
                                                                @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(quizService.getRanking(id,page, size, startDate, endDate));
    }

    @GetMapping("/daily/{id}")
    public ResponseEntity<ResponseDailyQuestionsDto> daily(@PathVariable String id){
        return ResponseEntity.ok(quizService.getDailyQuestions(id));
    }
}
