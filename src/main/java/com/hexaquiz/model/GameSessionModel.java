package com.hexaquiz.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_session")
public class GameSessionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int index;

    private int points;

    private boolean finished;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

}
