package com.deanery.app.dto;

import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Individual;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewIndividualDto {

    private UUID id;

    private Integer individual_code;

    private String first_name;

    private String second_name;

    private String patronymic;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private Gender gender;

    private String birthplace;

    private String snils;

    private String inn;

    private String registration;

    private String actualAddress;

    private String email;

    public ViewIndividualDto(Individual individual) {
        this.id = individual.getId();
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
