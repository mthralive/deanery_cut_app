package com.deanery.app.repository;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.Enums.Status;
import com.deanery.app.model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EducationPlanRepository extends JpaRepository<EducationPlan, UUID> {
    EducationPlan findByNum(Integer num);

    @Query("Select e " +
            "from educationPlan e " +
            "where e.FullName like CONCAT('%',:fullName,'%') " +
            "and e.status = 'ACTIVE' order by e.FullName")
    List<EducationPlan> findAllEP(@Param("fullName") String fullName);

    @Query("Select wp.workPlan.educationPlan " +
            "from individualWorkPlan wp " +
            "where wp.individual.id != :ind and" +
            " wp.workPlan.educationPlan.FullName like CONCAT('%',:fullName,'%') " +
            "and wp.workPlan.educationPlan.status = 'ACTIVE'" +
            " order by wp.workPlan.educationPlan.FullName")
    List<EducationPlan> findAllEPWithoutInd(@Param("fullName") String fullName, @Param("ind") UUID ind);

    @Query("select wp.individual " +
            "from individualWorkPlan wp " +
            "where wp.workPlan.educationPlan.id = :ep " +
            "and wp.status != 'EXPELLED' " +
            "and wp.status != 'TRANSFERRED' " +
            "and wp.status != 'ACADEM' ")
    List<Individual> findAllIndividuals(@Param("ep") UUID ep);
}
