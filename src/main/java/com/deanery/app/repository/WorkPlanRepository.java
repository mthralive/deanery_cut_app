package com.deanery.app.repository;

import com.deanery.app.model.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkPlanRepository extends JpaRepository<WorkPlan, UUID> {
}
