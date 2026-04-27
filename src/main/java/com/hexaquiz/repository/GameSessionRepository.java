package com.hexaquiz.repository;

import com.hexaquiz.model.GameSessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameSessionRepository extends JpaRepository<GameSessionModel, UUID> {
}
