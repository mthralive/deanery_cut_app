package com.deanery.app.repository;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkPlanRepository extends JpaRepository<WorkPlan, UUID> {
    List<WorkPlan> findByEducationPlan_Id(UUID id);
}
