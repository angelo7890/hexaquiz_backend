package com.hexaquiz.controller;

import com.hexaquiz.dto.request.RequestCreateQuestionDto;
import com.hexaquiz.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hexaquiz/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RequestCreateQuestionDto request ){
        questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
