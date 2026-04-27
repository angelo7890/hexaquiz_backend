package com.hexaquiz.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "questions")
public class QuestionsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Lob
    @Column(nullable = false)
    private String text;

    private int type;

    private String answer;

    private int basePoints;

    private String image;
}
