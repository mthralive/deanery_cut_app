package com.deanery.app;

import com.deanery.app.dto.EducationPlanDto;
import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.Enums.*;
import com.deanery.app.model.Individual;
import com.deanery.app.model.User;
import com.deanery.app.repository.EducationPlanRepository;
import com.deanery.app.repository.IndividualRepository;
import com.deanery.app.repository.UserRepository;
import com.deanery.app.service.EducationPlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EducationPlanH2Test {

    @Autowired
    private EducationPlanService educationPlanService;

    @Autowired
    private EducationPlanRepository educationPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IndividualRepository individualRepository;

    @Test
    public void testFindEducationPlan() {

        Individual individual = individualRepository.save(new Individual(
                UUID.randomUUID(),
                123,
                "Jane",
                "Doe",
                "Smith",
                LocalDate.of(1990, 1, 1),
                Gender.FEMALE,
                "City",
                "123-45-6789",
                "123-45-6789",
                "Address",
                "Actual Address",
                "jane.doe@example.com"
        ));
        User user = userRepository.save(new User(UUID.randomUUID(), "jane.doe@example.com", "12345678", individual, UserRole.ADMIN, 1000, true));
        EducationPlan plan = educationPlanRepository.save(new EducationPlan(
                null, 1, LocalDate.now(), 2020, 2024, 4,
                "Test Plan", "TP", EducationType.BACALAVR, EducationForm.EDU_IN,
                EducationQual.HIGH, Status.ACTIVE));

        EducationPlan result = educationPlanService.findEducationPlan(plan.getId());
        assertNotNull(result);
    }

    @Test
    public void testFindAllEduPlansList() {
        EducationPlan plan1 = new EducationPlan(null, 1, LocalDate.now(), 2020, 2024, 4,
                "Plan 1", "P1", EducationType.BACALAVR, EducationForm.EDU_IN,
                EducationQual.HIGH, Status.ACTIVE);
        EducationPlan plan2 = new EducationPlan(null, 2, LocalDate.now(), 2018, 2022, 4,
                "Plan 2", "P2", EducationType.MAGISTR, EducationForm.EDU_OUT_IN,
                EducationQual.HIGH, Status.INACTIVE);
        educationPlanRepository.save(plan1);
        educationPlanRepository.save(plan2);

        List<EducationPlan> plans = educationPlanService.findAllEduPlansList();
        assertNotNull(plans);
        assertTrue(plans.size() >= 2);
    }

    @Test
    public void testCreateEducationPlan() {
        EducationPlanDto dto = new EducationPlanDto();
        dto.setYear_from(2021);
        dto.setYear_to(2025);
        dto.setFullName("New Education Plan");
        dto.setEdu_type(EducationType.BACALAVR);
        dto.setEdu_form(EducationForm.EDU_IN);
        dto.setEdu_qual(EducationQual.HIGH);

        EducationPlan createdPlan = educationPlanService.create(dto);
        assertNotNull(createdPlan);
        assertEquals(dto.getFullName(), createdPlan.getFullName());
        assertEquals(2025 - 2021, createdPlan.getEdu_years());
    }

    @Test
    public void testUpdateEducationPlan() {
        UUID testId = UUID.randomUUID();
        EducationPlan plan = educationPlanRepository.save(new EducationPlan(
                null, 1, LocalDate.now(), 2020, 2024, 4,
                "Original Plan", "OP", EducationType.BACALAVR, EducationForm.EDU_IN,
                EducationQual.HIGH, Status.ACTIVE));

        EducationPlanDto updateDto = new EducationPlanDto();
        updateDto.setYear_from(2021);
        updateDto.setYear_to(2025);
        updateDto.setFullName("Updated Plan");
        updateDto.setEdu_type(EducationType.MAGISTR);
        updateDto.setEdu_form(EducationForm.EDU_IN);
        updateDto.setEdu_qual(EducationQual.HIGH);

        EducationPlan updatedPlan = educationPlanService.update(plan.getId(), updateDto);
        assertNotNull(updatedPlan);
        assertEquals("Updated Plan", updatedPlan.getFullName());
        assertEquals(2025 - 2021, updatedPlan.getEdu_years());
        assertEquals(EducationType.MAGISTR, updatedPlan.getEdu_type());
    }

    @Test
    public void testDeleteEducationPlan() {
        UUID testId = UUID.randomUUID();
        EducationPlan plan = new EducationPlan(
                null, 1, LocalDate.now(), 2020, 2024, 4,
                "Plan To Delete", "PTD", EducationType.BACALAVR, EducationForm.EDU_IN,
                EducationQual.HIGH, Status.ACTIVE);
        educationPlanRepository.save(plan);

        educationPlanService.delete(plan.getId());
        assertFalse(educationPlanRepository.findById(plan.getId()).isPresent());
    }
}
