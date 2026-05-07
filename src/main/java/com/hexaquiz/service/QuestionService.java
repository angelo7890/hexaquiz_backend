package com.hexaquiz.service;

import com.hexaquiz.dto.option.OptionDto;
import com.hexaquiz.dto.request.RequestCreateQuestionDto;
import com.hexaquiz.model.OptionModel;
import com.hexaquiz.model.QuestionModel;
import com.hexaquiz.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void createQuestion(RequestCreateQuestionDto dto){
        QuestionModel question = new QuestionModel();
        question.setText(dto.text());
        question.setType(dto.type());
        question.setAnswer(dto.answer());
        question.setBasePoints(dto.basePoints());
        question.setImage(dto.image());
        question.setScheduledDate(dto.scheduledDate());
        question.setSequence(dto.sequence());
        Optional<QuestionModel> questionOpt =
                questionRepository.findByScheduledDate(dto.scheduledDate());
        if(questionOpt.isPresent()){
            var questionDataBase = questionOpt.get();
            question.setQuizId(questionDataBase.getQuizId());
        }else {
            question.setQuizId(UUID.randomUUID().toString());
        }
        if(dto.options() != null){
            for(OptionDto optionDto : dto.options()){
                OptionModel option = new OptionModel();
                option.setText(optionDto.text());
                option.setImage(optionDto.image());
                question.addOption(option);
            }
        }
        questionRepository.save(question);
    }
}
