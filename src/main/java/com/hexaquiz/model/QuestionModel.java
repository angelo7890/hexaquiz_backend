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
        this.quizId = quizId;
    }
    public void addOption(OptionModel option) {
        options.add(option);
        option.setQuestion(this);
    }

    public UUID getId() {
        return id;
    }

    public List<OptionModel> getOptions() {
        return options;
    }

    public void setOptions(List<OptionModel> options) {
        this.options = options;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionTypeEnum getType() {
        return type;
    }

    public void setType(QuestionTypeEnum type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getBasePoints() {
        return basePoints;
    }

    public void setBasePoints(int basePoints) {
        this.basePoints = basePoints;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
