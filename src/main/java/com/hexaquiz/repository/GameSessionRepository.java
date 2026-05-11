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
    GROUP BY u.id, u.username, u.name, u.profileImage
    ORDER BY SUM(g.points) DESC, MAX(g.completedAt) ASC
    """)
    Page<RankingDto> getGeneralRanking(Pageable pageable);

    @Query(value = """
    SELECT position FROM ( SELECT 
            u.id,
            RANK() OVER (
                ORDER BY
                    COALESCE(SUM(g.points), 0) DESC,
                    MAX(g.completed_at) ASC
            ) AS position
        FROM game_session g
        JOIN users u ON u.id = g.user_id
        GROUP BY u.id
    ) ranked_users
    WHERE ranked_users.id = :userId
    """, nativeQuery = true)
    Long getUserRankingPosition(UUID userId);

    GameSessionModel findByid(UUID id);

    Optional<GameSessionModel> findByUserIdAndFinishedFalse(UUID userId);

    boolean existsByUserIdAndFinishedFalse(UUID userId);

    Optional<GameSessionModel> findByUserIdAndQuizId(UUID userId, String quizId);
}
