package com.hexaquiz.repository;

import com.hexaquiz.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    @Query("SELECT COALESCE(SUM(g.points), 0) FROM GameSessionModel g WHERE g.user.id = :userId")
    Integer sumPointsByUserId(UUID userId);

    @Query("SELECT COUNT(g) FROM GameSessionModel g WHERE g.user.id = :userId AND g.finished = true")
    Integer countFinishedByUserId(UUID userId);

    boolean existsByusername(String username);

    UserModel findByid(UUID id);
}
