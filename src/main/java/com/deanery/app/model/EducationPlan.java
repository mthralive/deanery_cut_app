package com.deanery.app.model;

import com.deanery.app.model.Enums.EducationForm;
import com.deanery.app.model.Enums.EducationQual;
import com.deanery.app.model.Enums.EducationType;
import com.deanery.app.model.Enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "educationPlan")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EducationPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private Integer num;

    @Column(nullable = false)
    private LocalDate create_date;

    @Column(nullable = false)
    private Integer Year_from;

    @Column(nullable = false)
    private Integer Year_to;

    @Column(nullable = false)
    private Integer edu_years;

    @Column(nullable = false)
    private String FullName;

    @Column(nullable = false)
    private String ShortName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationType edu_type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationForm edu_form;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EducationQual edu_qual;
}
