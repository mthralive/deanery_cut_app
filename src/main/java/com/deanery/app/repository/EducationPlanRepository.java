package com.deanery.app.repository;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.Enums.Status;
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

//    @Query(value ="SELECT e.* " +
//            "FROM education_plan e" +
//            "WHERE e.full_name LIKE CONCAT('%', 'Программная инженерия', '%')" +
//            " AND e.status = 'ACTIVE'" +
//            "ORDER BY e.full_name", nativeQuery = true)
//    List<EducationPlan> findEP(@Param("fullName") String fullName);
}
