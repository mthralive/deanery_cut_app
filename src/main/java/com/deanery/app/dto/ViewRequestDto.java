package com.deanery.app.dto;

import com.deanery.app.model.Enums.RequestStatus;
import com.deanery.app.model.Individual;
import com.deanery.app.model.Request;
import com.deanery.app.model.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ViewRequestDto {

    private UUID id;

    private LocalDate date;

    private String request_text;

    private RequestStatus status;

    private Individual individual;

    private User user;

    public ViewRequestDto(Request request) {
        this.id = request.getId();
        this.date = request.getDate();
        this.request_text = request.getRequest_text();
        this.status = request.getStatus();
        this.individual = request.getIndividual();
        this.user = request.getUser();
    }
}
