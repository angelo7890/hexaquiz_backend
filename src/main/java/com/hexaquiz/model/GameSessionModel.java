package com.hexaquiz.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(name = "game_session")
public class GameSessionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    private String quizId;

    private int gameSessionIndex;

    private int points;

    private boolean finished;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(updatable = false)
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
    }

    public GameSessionModel() {}

    public GameSessionModel(int gameSessionIndex, String quizId) {
        this.gameSessionIndex = gameSessionIndex;
        this.quizId = quizId;
        this.points = 0;
        this.finished = false;
        this.completedAt = null;
    }

    public UUID getId() {
        return id;
    }



    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getGameSessionIndex() {
        return gameSessionIndex;
    }

    public void setGameSessionIndex(int gameSessionIndex) {
        this.gameSessionIndex = gameSessionIndex;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
