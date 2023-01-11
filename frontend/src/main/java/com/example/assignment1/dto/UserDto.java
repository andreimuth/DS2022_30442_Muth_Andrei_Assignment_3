package com.example.assignment1.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UserDto {

    private Long id;
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @NotNull(message = "Username cannot be null")
    public String username;
    @Pattern(message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character",
            regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@.$%^&*-]).{8,}$")
    @NotNull(message = "Password cannot be null")
    private String password;
    @NotNull(message = "Role cannot be null")
    private String role;

}
