package com.deanery.app;

import com.deanery.app.dto.IndividualDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.error.exception.ValidationException;
import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Individual;
import com.deanery.app.repository.*;
import com.deanery.app.service.IndividualService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndividualServiceTest {

    @Mock
    private IndividualRepository individualRepository;

    @Mock
    private IndividualWorkPlanRepository individualWorkPlanRepository;

    @Mock
    private WorkPlanRepository workPlanRepository;

    @Mock
    private LogInfoRepository logInfoRepository;

    @Mock
    private EducationPlanRepository educationPlanRepository;

    @InjectMocks
    private IndividualService individualService;

    private IndividualDto individualDto;
    private Individual individual;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        individual = new Individual(UUID.randomUUID(), 123, "John", "Doe", "Smith", LocalDate.of(1990, 1, 1), Gender.MALE, "Moscow", "123-45-6789", "123-45-6789", "Moscow", "Moscow", "john.doe@example.com");
        individualDto = new IndividualDto(new Individual(null, 123, "Updated", "Doe", "Smith", LocalDate.now(), Gender.MALE, "Moscow", "123-45-6789", "123-45-6789", "Moscow", "Moscow", "john.doe@example.com"));
    }

    @Test
    void testFindBySnils_IndividualExists() {
        // Arrange
        String snils = "123-45-6789";
        when(individualRepository.findBySnils(snils)).thenReturn(individual);

        // Act
        Individual result = individualService.findBySnils(snils);

        // Assert
        assertNotNull(result);
        assertEquals(individual.getSnils(), result.getSnils());
    }

    @Test
    void testFindBySnils_IndividualNotFound() {
        // Arrange
        String snils = "123-45-6789";
        when(individualRepository.findBySnils(snils)).thenReturn(null);

        // Act
        Individual result = individualService.findBySnils(snils);

        // Assert
        assertNull(result);
    }

    @Test
    void testCreate_NewIndividual() {
        // Arrange
        when(individualRepository.findBySnils(individualDto.getSnils())).thenReturn(null);
        when(individualRepository.save(any(Individual.class))).thenReturn(individual);

        // Act
        Individual result = individualService.create(individualDto);

        // Assert
        assertNotNull(result);
        assertEquals(individualDto.getSnils(), result.getSnils());
    }

    @Test
    void testUpdateIndividual() {
        // Arrange
        UUID id = individual.getId();
        IndividualDto updatedDto = new IndividualDto(new Individual(null, 123, "Updated", "Doe", "Smith", LocalDate.now(), Gender.MALE, "Moscow", "123-45-6789", "123-45-6789", "Moscow", "Moscow", "john.doe@example.com"));
        when(individualRepository.findById(id)).thenReturn(Optional.of(individual));
        when(individualRepository.save(any(Individual.class))).thenReturn(individual);

        // Act
        Individual result = individualService.updateIndividual(id, updatedDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated", result.getFirst_name());
    }

    @Test
    void testDeleteIndividual() {
        // Arrange
        UUID id = individual.getId();
        when(individualRepository.findById(id)).thenReturn(Optional.of(individual));

        // Act
        Individual result = individualService.deleteIndividual(id);

        // Assert
        assertNotNull(result);
        verify(individualRepository).deleteById(id);
    }

    @Test
    void testFindIndividual() {
        // Arrange
        UUID id = individual.getId();
        when(individualRepository.findById(id)).thenReturn(Optional.of(individual));

        // Act
        Individual result = individualService.findIndividual(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindIndividual_NotFound() {
        // Arrange
        UUID id = individual.getId();
        when(individualRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> individualService.findIndividual(id));
    }

    @Test
    void testCreatePersonalCode() {
        // Arrange
        UUID id = individual.getId();
        when(individualRepository.findById(id)).thenReturn(Optional.of(individual));
        when(individualRepository.save(any(Individual.class))).thenReturn(individual);

        // Act
        Individual result = individualService.createPersonalCode(id);

        // Assert
        assertNotNull(result);
        assertEquals(individual.hashCode(), result.getIndividualCode());
    }

}
