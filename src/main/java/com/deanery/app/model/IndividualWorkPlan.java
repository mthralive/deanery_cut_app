package com.deanery.app.model;

import com.deanery.app.model.Enums.WorkPlanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.UUID;

@Entity(name = "individualWorkPlan")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IndividualWorkPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "individual_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Individual individual;

    @ManyToOne
    @JoinColumn(name = "workPlan_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private WorkPlan workPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkPlanStatus status;
}
