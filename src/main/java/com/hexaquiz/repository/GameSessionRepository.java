package com.hexaquiz.repository;

import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.model.GameSessionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSessionModel, UUID> {

    @Query("""
        SELECT new com.hexaquiz.dto.ranking.RankingDto(
            u.id,
            u.username,
            u.name,
            u.profileImage,
            SUM(g.points)
        )
        FROM GameSessionModel g
        JOIN g.user u
        WHERE g.finished = true
        GROUP BY u.id, u.username, u.name, u.profileImage
        ORDER BY SUM(g.points) DESC
    """)
    Page<RankingDto> getGeneralRanking(Pageable pageable);

    GameSessionModel findByid(UUID id);

    Optional<GameSessionModel> findByUserIdAndFinishedFalse(UUID userId);

    boolean existsByUserIdAndFinishedFalse(UUID userId);
}
