package com.deanery.app.dto;

import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Individual;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class IndividualDto {

    private Integer individual_code;

    @NotBlank(message = "First name is required")
    private String first_name;

    @NotBlank(message = "Second name is required")
    private String second_name;

    @NotBlank(message = "Patronymic is required")
    private String patronymic;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "BirthPlace is required")
    private String birthplace;

    @NotBlank(message = "Snils is required")
    private String snils;

    @NotBlank(message = "Inn is required")
    private String inn;

    @NotBlank(message = "Registration is required")
    private String registration;

    @NotBlank(message = "Address is required")
    private String actualAddress;

    @NotBlank(message = "Email is required")
    private String email;

    public IndividualDto(Individual individual) {
        this.individual_code = individual.getIndividualCode();
        this.first_name = individual.getFirst_name();
        this.second_name = individual.getSecond_name();
        this.patronymic = individual.getPatronymic();
        this.birthday = individual.getBirthday();
        this.gender = individual.getGender();
        this.birthplace = individual.getBirthplace();
        this.snils = individual.getSnils();
        this.inn = individual.getInn();
        this.registration = individual.getRegistration();
        this.actualAddress = individual.getActualAddress();
        this.email = individual.getEmail();
    }
}
