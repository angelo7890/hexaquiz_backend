package com.hexaquiz.repository;

import com.hexaquiz.model.OptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OptionRepository extends JpaRepository<OptionModel, UUID> {
}
