package com.deanery.app.model;

import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "individual")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Individual {

    //TODO Добавить фото + связь с рабочим планом
    //TODO генерация кода физ лица

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(length = 8)
    private String individualCode;

    @Column(nullable = false, length = 64)
    private String first_name;

    @Column(nullable = false, length = 64)
    private String second_name;

    @Column(nullable = false, length = 64)
    private String patronymic;

    @Column(nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 64)
    private String birthplace;

    @Column(nullable = false, length = 64)
    private String snils;

    @Column(nullable = false, length = 64)
    private String inn;

    @Column(nullable = false, length = 64)
    private String registration;

    @Column(nullable = false, length = 64)
    private String actualAddress;

    @Column(nullable = false, unique = true, length = 64)
    @Size(min = 7, max = 64)
    private String email;
}
