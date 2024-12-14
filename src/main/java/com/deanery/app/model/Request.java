package com.deanery.app.model;

import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Enums.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "request")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 1024)
    private String request_text;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private User user;

    @ManyToOne
    @JoinColumn(name = "individual_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Individual individual;

    public Request(String testRequest, User user, Individual individual) {
        this.request_text = testRequest;
        this.user = user;
        this.individual = individual;
    }
}
