package com.deanery.app.repository;

import com.deanery.app.model.IndividualWorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IndividualWorkPlanRepository extends JpaRepository<IndividualWorkPlan, UUID> {
}
