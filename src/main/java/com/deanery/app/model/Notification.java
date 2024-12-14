package com.deanery.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.UUID;

@Entity(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    //todo связь с юзером

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 1024)
    @Size(min=8)
    private String text;

    @Column
    private Boolean isChecked;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private User user;


}
