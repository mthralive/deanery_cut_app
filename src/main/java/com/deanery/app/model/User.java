package com.deanery.app.model;

import com.deanery.app.model.Enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.UUID;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    @Size(min = 7, max = 64)
    private String email;

    @Column(nullable = false, length = 64)
    @Size(min = 8, max = 64)
    private String password;

    @OneToOne
    @JoinColumn(name = "individual_id")
    @Fetch(FetchMode.JOIN)
    private Individual individual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Boolean isActive;
}
