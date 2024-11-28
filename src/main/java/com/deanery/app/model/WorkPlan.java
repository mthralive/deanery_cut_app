package com.deanery.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.UUID;

@Entity(name = "workPlan")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "educationPlan_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private EducationPlan educationPlan;


}
