package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Pattern(regexp="^(?=.{2,20}$)[a-zA-Z0-9_][a-zA-Z0-9_.]*",
            message = "Usernames can only use letters, numbers, underscores and periods.")
    private String username;

    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    private String password;
}
