package com.hexaquiz.controller;

import com.hexaquiz.dto.request.RequestCreateQuestionDto;
import com.hexaquiz.dto.response.ResponseQuestionsDto;
import com.hexaquiz.service.QuestionService;
import com.hexaquiz.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/hexaquiz/question")
public class QuestionController {

    private final QuestionService questionService;
    private final QuizService quizService;

    public QuestionController(QuestionService questionService, QuizService quizService) {
        this.questionService = questionService;
        this.quizService = quizService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RequestCreateQuestionDto request ){
        questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/day")
    public ResponseEntity<ResponseQuestionsDto> getQuestions(@RequestParam LocalDate localDate){
        return ResponseEntity.ok(quizService.getQuestions(localDate));
    }
}
