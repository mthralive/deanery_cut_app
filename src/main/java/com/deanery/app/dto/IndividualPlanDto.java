package com.deanery.app.dto;

import com.deanery.app.model.Enums.WorkPlanStatus;
import com.deanery.app.model.Individual;
import com.deanery.app.model.IndividualWorkPlan;
import com.deanery.app.model.WorkPlan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndividualPlanDto {
    private Individual individual;

    private WorkPlan workPlan;

    private WorkPlanStatus status;

    public IndividualPlanDto(IndividualWorkPlan individualWorkPlan) {
        this.individual = individualWorkPlan.getIndividual();
        this.workPlan = individualWorkPlan.getWorkPlan();
        this.status = individualWorkPlan.getStatus();
    }
}
