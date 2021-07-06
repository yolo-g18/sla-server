package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String firstname;
    private String lastname;
    private String address;
    private String avatar;
    private String job;
    private String schoolName;
    private String major;
    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;
    @Size(max = 200, message = "Bio is not over 200 character")
    private String bio;
}
