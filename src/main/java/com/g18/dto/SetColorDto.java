package com.g18.dto;

import com.g18.model.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetColorDto {

    private Long id;
    @Enumerated(EnumType.STRING)
    private Color color;
}
