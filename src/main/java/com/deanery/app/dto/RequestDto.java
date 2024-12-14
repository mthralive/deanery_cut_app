package com.deanery.app.dto;

import com.deanery.app.model.Enums.RequestStatus;
import com.deanery.app.model.Individual;
import com.deanery.app.model.Request;
import com.deanery.app.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RequestDto {

    @NotBlank
    private String request_text;

    private RequestStatus status;

    @NotNull(message = "Individual is required")
    private Individual individual;

    @NotNull(message = "User is required")
    private User user;

    public RequestDto(Request request) {
        this.request_text = request.getRequest_text();
        this.status = request.getStatus();
        this.user = request.getUser();
        this.individual = request.getIndividual();
    }
}
