package com.example.assignment1.security.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder

public class SignupRequest {
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Pattern(message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character",
            regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@.$%^&*-]).{8,}$")
    private String password;
}
