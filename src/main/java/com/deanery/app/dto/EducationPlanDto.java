package com.deanery.app.dto;

import com.deanery.app.model.EducationPlan;
import com.deanery.app.model.Enums.EducationForm;
import com.deanery.app.model.Enums.EducationQual;
import com.deanery.app.model.Enums.EducationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class EducationPlanDto {

    private Integer Num;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate create_date;

    @NotNull(message = "Year From is required")
    private Integer year_from;

    @NotNull(message = "Year To is required")
    private Integer year_to;

    private Integer edu_years;

    @NotBlank(message = "FullName is required")
    private String fullName;

    private String shortName;

    @NotNull(message = "Education type is required")
    private EducationType edu_type;

    @NotNull(message = "Education form is required")
    private EducationForm edu_form;

    @NotNull(message = "Education qualification is required")
    private EducationQual edu_qual;

    @Override
    public int hashCode() {
        Integer hash = Objects.hash(fullName, year_from, year_to);
        if(hash > 0){
            return hash;
        }
        return hash * -1;
    }

    public EducationPlanDto(EducationPlan educationPlan) {
        this.Num = educationPlan.getNum();
        this.create_date = educationPlan.getCreate_date();
        this.year_from = educationPlan.getYear_from();
        this.year_to = educationPlan.getYear_to();
        this.edu_years = educationPlan.getEdu_years();
        this.fullName = educationPlan.getFullName();
        this.shortName = educationPlan.getShortName();
        this.edu_type = educationPlan.getEdu_type();
        this.edu_form = educationPlan.getEdu_form();
        this.edu_qual = educationPlan.getEdu_qual();
    }
}
