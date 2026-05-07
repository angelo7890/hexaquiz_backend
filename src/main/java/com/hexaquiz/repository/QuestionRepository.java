package com.hexaquiz.repository;
import com.hexaquiz.model.QuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel, UUID> {
    Optional<QuestionModel> findByScheduledDate(LocalDate date);

    Optional<QuestionModel> findById(UUID id);

    List<QuestionModel> findByScheduledDateOrderBySequenceAsc(LocalDate date);
}
