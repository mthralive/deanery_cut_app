package com.deanery.app.dto;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.WorkPlan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ViewWorkPlanDto {

    private UUID id;

    private String name;

    private Integer course;

    private EducationPlan educationPlan;

    public ViewWorkPlanDto(WorkPlan workPlan) {
        this.id = workPlan.getId();
        this.name = workPlan.getName();
        this.course = workPlan.getCourse();
        this.educationPlan = workPlan.getEducationPlan();
    }
}
