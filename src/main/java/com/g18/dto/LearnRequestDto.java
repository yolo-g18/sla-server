package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearnRequestDto {

    @NotNull
    private Long studySetId;

    @NotNull
    @NotBlank
    private String learnDate;
}
