package com.deanery.app.model;

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
    private String Num;

    @Column(nullable = false)
    private LocalDate Date;

    @Column(nullable = false)
    private String FullName;

    @Column(nullable = false)
    private String ShortName;
}
