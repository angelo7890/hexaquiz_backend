package com.hexaquiz.repository;

import com.hexaquiz.model.GameSessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSessionModel, UUID> {
    GameSessionModel findByid(UUID id);
}
