package com.g18.dto;

import com.g18.entity.StudySet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Long id;
    private Long studySetID;
    private String front;
    private String back;

}
