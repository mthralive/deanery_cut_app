package com.deanery.app;

import com.deanery.app.dto.EducationPlanDto;
import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.error.exception.ValidationException;
import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.WorkPlan;
import com.deanery.app.repository.EducationPlanRepository;
import com.deanery.app.repository.IndividualWorkPlanRepository;
import com.deanery.app.repository.WorkPlanRepository;
import com.deanery.app.service.EducationPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EducationPlanServiceTest {

    @Mock
    private EducationPlanRepository educationPlanRepository;

    @Mock
    private WorkPlanRepository workPlanRepository;

    @Mock
    private IndividualWorkPlanRepository individualWorkPlanRepository;

    @InjectMocks
    private EducationPlanService educationPlanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findEducationPlan_ShouldReturnPlan_WhenFound() {
        UUID id = UUID.randomUUID();
        EducationPlan mockPlan = new EducationPlan();
        when(educationPlanRepository.findById(id)).thenReturn(Optional.of(mockPlan));

        EducationPlan result = educationPlanService.findEducationPlan(id);

        assertNotNull(result);
        verify(educationPlanRepository).findById(id);
    }

    @Test
    void findEducationPlan_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(educationPlanRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> educationPlanService.findEducationPlan(id));
        verify(educationPlanRepository).findById(id);
    }

    @Test
    void create_ShouldSaveNewPlan_WhenPlanDoesNotExist() {
        EducationPlanDto dto = new EducationPlanDto();
        dto.setFullName("Test Plan");
        dto.setYear_from(2020);
        dto.setYear_to(2024);
        when(educationPlanRepository.findByNum(dto.hashCode())).thenReturn(null);

        EducationPlan savedPlan = new EducationPlan();
        when(educationPlanRepository.save(any(EducationPlan.class))).thenReturn(savedPlan);

        EducationPlan result = educationPlanService.create(dto);

        assertNotNull(result);
        verify(educationPlanRepository).findByNum(dto.hashCode());
        verify(educationPlanRepository).save(any(EducationPlan.class));
    }

    @Test
    void delete_ShouldRemovePlan_WhenNoWorkPlansExist() {
        UUID id = UUID.randomUUID();
        EducationPlan mockPlan = new EducationPlan();
        when(educationPlanRepository.findById(id)).thenReturn(Optional.of(mockPlan));
        when(workPlanRepository.findByEducationPlan_IdOrderByCourse(id)).thenReturn(List.of());

        EducationPlan result = educationPlanService.delete(id);

        assertNotNull(result);
        verify(educationPlanRepository).deleteById(id);
    }

    @Test
    void delete_ShouldRemoveWorkPlansAndPlan_WhenWorkPlansExist() {
        UUID id = UUID.randomUUID();
        EducationPlan mockPlan = new EducationPlan();
        when(educationPlanRepository.findById(id)).thenReturn(Optional.of(mockPlan));
        when(workPlanRepository.findByEducationPlan_IdOrderByCourse(id)).thenReturn(List.of(new WorkPlan()));

        EducationPlan result = educationPlanService.delete(id);

        assertNotNull(result);
        verify(workPlanRepository).deleteAllByEducationPlan_Id(id);
        verify(educationPlanRepository).deleteById(id);
    }

    @Test
    void delete_ShouldThrowException_WhenPlanNotFound() {
        UUID id = UUID.randomUUID();
        when(educationPlanRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> educationPlanService.delete(id));
        verify(educationPlanRepository).findById(id);
    }
}
