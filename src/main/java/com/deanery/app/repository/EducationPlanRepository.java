package com.deanery.app.repository;

import com.deanery.app.model.EducationPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducationPlanRepository extends JpaRepository<EducationPlan, UUID> {
    EducationPlan findByNum(Integer num);
}
