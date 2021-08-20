package com.g18.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    private Long id;
    private Long studySet;

    @NotBlank(message = "Front is required")
    @NotNull(message = "Front is required")
    private String front;

    @NotBlank(message = "Back is required")
    @NotNull(message = "Back is required")
    private String back;

    private String hint;

}
