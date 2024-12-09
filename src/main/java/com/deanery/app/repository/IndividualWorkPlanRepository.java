package com.deanery.app.repository;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.IndividualWorkPlan;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IndividualWorkPlanRepository extends JpaRepository<IndividualWorkPlan, UUID> {
    @Query("Select e " +
            "from individualWorkPlan e " +
            "where e.individual.id = :indId " +
            "and e.workPlan.educationPlan.id = :epId")
    IndividualWorkPlan findAllWP(@Param("indId") UUID iid, @Param("epId") UUID eid);

    List<IndividualWorkPlan> findAllByIndividual_Id(UUID id);
    List<IndividualWorkPlan> findAllByWorkPlan_EducationPlan_Id(UUID id);
}
