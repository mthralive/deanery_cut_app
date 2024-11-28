package com.deanery.app.dto.user;

import com.deanery.app.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpDto {
    @NotBlank(message = "Email is required")
    @Size(min = 7, max = 64)
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Incorrect email value")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64)
    private String password;

    @NotBlank(message = "Password confirm is required")
    @Size(min = 8, max = 64)
    private String passwordConfirm;

    private Boolean isActive;

    public UserSignUpDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.passwordConfirm = user.getPassword();
        this.isActive = user.getIsActive();
    }
}
