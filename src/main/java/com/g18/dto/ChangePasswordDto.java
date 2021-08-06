package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    @NotBlank(message = "Password is required")
    @NotNull(message = "Password is required")
    private String newPassword;

    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    @NotBlank(message = "Password is required")
    @NotNull(message = "Password is required")
    private String oldPassword;
}
