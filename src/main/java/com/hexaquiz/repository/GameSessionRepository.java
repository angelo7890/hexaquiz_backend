package com.hexaquiz.repository;

import com.hexaquiz.dto.log.LogDto;
import com.hexaquiz.dto.ranking.RankingDto;
import com.hexaquiz.model.GameSessionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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

    @Query("""
    SELECT DISTINCT new com.hexaquiz.dto.log.LogDto(
        u.id,
        u.username,
        u.name,
        u.profileImage
    )
    FROM GameSessionModel g
    JOIN g.user u
    WHERE g.completedAt >= :start
    AND g.completedAt < :end
    """)
    List<LogDto> findUsersByDate(LocalDateTime start, LocalDateTime end);

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
    WHERE g.completedAt >= :start
    AND g.completedAt < :end
    GROUP BY u.id, u.username, u.name, u.profileImage
    ORDER BY
        COALESCE(SUM(g.points), 0L) DESC,
        MIN(g.completedAt) ASC
    """)
    Page<RankingDto> getWeeklyRanking(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query(value = """
    WITH ranking AS (
        SELECT
            u.id AS user_id,
            RANK() OVER (
                ORDER BY
                    COALESCE(SUM(g.points), 0) DESC,
                    MIN(g.completed_at) ASC
            ) AS position
        FROM game_session g
        JOIN users u ON u.id = g.user_id
        WHERE g.completed_at >= :start
        AND g.completed_at < :end
        GROUP BY u.id
    )
    SELECT position
    FROM ranking
    WHERE user_id = :userId
    """, nativeQuery = true)
    Long getWeeklyUserRankingPosition(UUID userId, LocalDateTime start, LocalDateTime end);

    GameSessionModel findByid(UUID id);

    Optional<GameSessionModel> findByUserIdAndFinishedFalse(UUID userId);

    boolean existsByUserIdAndFinishedFalse(UUID userId);

    Optional<GameSessionModel> findByUserIdAndQuizId(UUID userId, String quizId);
}
