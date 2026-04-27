package com.hexaquiz.model;

import com.hexaquiz.enums.QuestionTypeEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class QuestionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OptionModel> options;

    @Lob
    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private QuestionTypeEnum type;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private int basePoints;

    private String image;

    private LocalDate scheduledDate;

    private int sequence;

    private String quizId;


    public QuestionModel() {}
    public QuestionModel(List<OptionModel> options, String text, QuestionTypeEnum type, String answer, int basePoints, String image, LocalDate scheduledDate, int sequence, String quizId) {
        this.options = options;
        this.text = text;
        this.type = type;
        this.answer = answer;
        this.basePoints = basePoints;
        this.image = image;
        this.scheduledDate = scheduledDate;
        this.sequence = sequence;
        this.quizId = setQuizId(scheduledDate);
    }

    private String setQuizId(LocalDate date){
        //pesquisar no banco se existe data igual a passada, se existir atribuir a quiz id o q esta la, se nao existir, criar um novo quizid
        return UUID.randomUUID().toString();
    }

    public void addOption(OptionModel option) {
        options.add(option);
        option.setQuestion(this);
    }
}
