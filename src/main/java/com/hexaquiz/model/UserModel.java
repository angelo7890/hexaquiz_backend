package com.hexaquiz.model;

import com.hexaquiz.enums.UserTypeEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameSessionModel> gameSessions;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private UserTypeEnum type;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void addGameSession(GameSessionModel session) {
        gameSessions.add(session);
        session.setUser(this);
    }

    public UserModel() {}


    public UserModel(String name, String username, String password, String profileImage, UserTypeEnum type) {
        this.name = name;
        this.username = username;
        this.gameSessions = new ArrayList<>();
        this.password = password;
        this.profileImage = profileImage;
        this.type = UserTypeEnum.USER;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<GameSessionModel> getGameSessions() {
        return gameSessions;
    }

    public void setGameSessions(List<GameSessionModel> gameSessions) {
        this.gameSessions = gameSessions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserTypeEnum getType() {
        return type;
    }

}
