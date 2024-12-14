package com.deanery.app;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Enums.Status;
import com.deanery.app.model.Enums.WorkPlanStatus;
import com.deanery.app.model.Individual;
import com.deanery.app.model.IndividualWorkPlan;
import com.deanery.app.model.WorkPlan;
import com.deanery.app.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@Rollback
public class IndividualServiceH2Test {

    @Autowired
    private IndividualRepository individualRepository;

    @Autowired
    private IndividualWorkPlanRepository individualWorkPlanRepository;

    @Autowired
    private WorkPlanRepository workPlanRepository;

    @Autowired
    private LogInfoRepository logInfoRepository;

    @Autowired
    private EducationPlanRepository educationPlanRepository;

    @Test
    public void testCreateIndividual() {
        Individual individual = individualRepository.save(new Individual(
                UUID.randomUUID(),
                12345678,
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

        Individual savedIndividual = individualRepository.save(individual);

        assertNotNull(savedIndividual.getId());
        assertEquals("Jane", savedIndividual.getFirst_name());
    }

    @Test
    public void testFindIndividualById() {
        Individual individual = new Individual(
                null, 87654321, "Jane", "Doe", "Smith", LocalDate.of(1992, 5, 15), Gender.FEMALE,
                "City", "987-654-321", "0123456789", "Address 3", "Address 4", "jane.doe@example.com"
        );

        Individual savedIndividual = individualRepository.save(individual);
        UUID id = savedIndividual.getId();

        Individual foundIndividual = individualRepository.findById(id).orElse(null);

        assertNotNull(foundIndividual);
        assertEquals("Jane", foundIndividual.getFirst_name());
    }

    @Test
    public void testUpdateIndividual() {
        Individual individual = new Individual(
                null, 67890000, "Alice", "Smith", "Johnson", LocalDate.of(1988, 8, 8), Gender.FEMALE,
                "Town", "456-789-123", "0192837465", "Address 5", "Address 6", "alice.smith@example.com"
        );

        Individual savedIndividual = individualRepository.save(individual);

        savedIndividual.setFirst_name("Alicia");
        individualRepository.save(savedIndividual);

        Individual updatedIndividual = individualRepository.findById(savedIndividual.getId()).orElse(null);

        assertNotNull(updatedIndividual);
        assertEquals("Alicia", updatedIndividual.getFirst_name());
    }

    @Test
    public void testDeleteIndividual() {
        Individual individual = new Individual(
                null, 11111111, "Bob", "Johnson", "Davis", LocalDate.of(1985, 10, 10), Gender.MALE,
                "Village", "111-222-333", "9988776655", "Address 7", "Address 8", "bob.johnson@example.com"
        );

        Individual savedIndividual = individualRepository.save(individual);
        UUID id = savedIndividual.getId();

        individualRepository.deleteById(id);

        Individual deletedIndividual = individualRepository.findById(id).orElse(null);

        assertNull(deletedIndividual);
    }

    @Test
    public void testCreateAndFindIndividualWorkPlan() {
        Individual individual = individualRepository.save(new Individual(
                null, 22222222, "Charlie", "Brown", "Taylor", LocalDate.of(1995, 3, 3), Gender.MALE,
                "City", "222-333-444", "1122334455", "Address 9", "Address 10", "charlie.brown@example.com"
        ));

        EducationPlan educationPlan = new EducationPlan();
        educationPlan.setFullName("Math Plan");
        educationPlan.setStatus(Status.INWORK);
        educationPlan = educationPlanRepository.save(educationPlan);

        WorkPlan workPlan = new WorkPlan();
        workPlan.setName("MP");

        workPlan.setCourse(1);
        workPlan.setEducationPlan(educationPlan);
        workPlan = workPlanRepository.save(workPlan);

        IndividualWorkPlan individualWorkPlan = new IndividualWorkPlan(null, individual, workPlan, WorkPlanStatus.STUDY);
        IndividualWorkPlan savedWorkPlan = individualWorkPlanRepository.save(individualWorkPlan);

        assertNotNull(savedWorkPlan.getId());
        assertEquals(WorkPlanStatus.STUDY, savedWorkPlan.getStatus());
    }

    @Test
    public void testFindAllIndividuals() {
        Individual individual1 = new Individual(
                null, 33333333, "Dave", "Green", "White", LocalDate.of(2000, 1, 1), Gender.MALE,
                "City", "333-444-555", "2233445566", "Address 11", "Address 12", "dave.green@example.com"
        );

        Individual individual2 = new Individual(
                null, 44444444, "Eve", "White", "Green", LocalDate.of(2002, 2, 2), Gender.FEMALE,
                "Town", "444-555-666", "3344556677", "Address 13", "Address 14", "eve.white@example.com"
        );

        individualRepository.save(individual1);
        individualRepository.save(individual2);

        List<Individual> individuals = individualRepository.findAll();

        assertTrue(individuals.size() >= 2);
    }
}
