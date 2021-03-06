package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @Pattern(regexp="^(?=.{2,20}$)[a-zA-Z0-9_][a-zA-Z0-9_.]*",
            message = "Usernames can only use letters, numbers, underscores and periods.")
    @NotBlank(message = "Username is required")
    @NotNull(message = "Username is required")
    private String username;

    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    @NotBlank(message = "Password is required")
    @NotNull(message = "Password is required")
    private String password;


}
