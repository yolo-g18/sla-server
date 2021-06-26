package com.g18.dto;

import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderRequest {

    /*  @NotBlank(message = "Email is required")
      @NotNull(message = "Email is required")
      @Email(message = "Please provide a valid email")
      private String email;

      @Pattern(regexp = "^(?=.{2,20}$)[a-zA-Z0-9_][a-zA-Z0-9_.]*",
              message = "Usernames can only use letters, numbers, underscores and periods.")
      @NotBlank(message = "Username is required")
      @NotNull(message = "Username is required")
      private String username;*/
    @Size(min = 10, max = 30, message = "Description must be between 10 and 30 characters")
    @NotBlank(message = "Description is required")
    @NotNull(message = "Description is required")
    private String title;

    @Size(max = 150, message = "Description must max 150 characters")
    @NotBlank(message = "Description is required")
    @NotNull(message = "Description is required")
    private String description;

    @Enumerated(EnumType.STRING)
    private Color color;
}
