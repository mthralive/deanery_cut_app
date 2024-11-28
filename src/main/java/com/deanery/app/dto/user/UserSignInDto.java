package com.deanery.app.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignInDto {
    @NotBlank(message = "Email is required")
    @Size(min = 7, max = 64)
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Incorrect email value")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64)
    private String password;

    @NotNull(message = "Otp is required")
    private Integer otp;
}

