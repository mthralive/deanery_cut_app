package com.deanery.app.repository;

import com.deanery.app.model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IndividualRepository extends JpaRepository<Individual, UUID> {
    Individual findBySnils(String snils);
}
