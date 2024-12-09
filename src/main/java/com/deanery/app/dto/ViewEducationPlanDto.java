package com.deanery.app.dto;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.Enums.EducationForm;
import com.deanery.app.model.Enums.EducationQual;
import com.deanery.app.model.Enums.EducationType;
import com.deanery.app.model.Enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewEducationPlanDto {

    private UUID id;

    private Integer num;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate create_date;

    private Integer year_from;

    private Integer year_to;

    private Integer edu_years;

    private String fullName;

    private String shortName;

    private EducationType edu_type;

    private EducationForm edu_form;

    private EducationQual edu_qual;
    private Status status;

    public ViewEducationPlanDto(EducationPlan educationPlan) {
        this.id = educationPlan.getId();
        this.num = educationPlan.getNum();
        this.create_date = educationPlan.getCreate_date();
        this.year_from = educationPlan.getYear_from();
        this.year_to = educationPlan.getYear_to();
        this.edu_years = educationPlan.getEdu_years();
        this.fullName = educationPlan.getFullName();
        this.shortName = educationPlan.getShortName();
        this.edu_type = educationPlan.getEdu_type();
        this.edu_form = educationPlan.getEdu_form();
        this.edu_qual = educationPlan.getEdu_qual();
        this.status =educationPlan.getStatus();
    }
}
