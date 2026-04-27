package com.hexaquiz.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "option")
public class OptionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionModel question;

    private String text;

    private String image;

    public OptionModel() {}

    public OptionModel(QuestionModel question, String text, String image) {
        this.question = question;
        this.text = text;
        this.image = image;
    }

    public UUID getId() {
        return id;
    }


    public QuestionModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionModel question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
