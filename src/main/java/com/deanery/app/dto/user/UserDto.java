package com.deanery.app.dto.user;

import com.deanery.app.model.Enums.Gender;
import com.deanery.app.model.Enums.UserRole;
import com.deanery.app.model.Individual;
import com.deanery.app.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Email is required")
    @Size(min = 7, max = 64)
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Incorrect email value")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64)
    private String password;

    @NotNull(message = "Role is required")
    private UserRole userRole;

    private Individual individual;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userRole = user.getRole();
        this.individual = user.getIndividual();
    }
}
