package com.deanery.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "logInfo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private UUID entityId;

    @Column(nullable = false)
    private String changeMessage;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private User user;
}
