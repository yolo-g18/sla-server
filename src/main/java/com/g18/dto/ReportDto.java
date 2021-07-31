package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long id;
    private Long ssId;
    private String ssTitle;
    private String username;
    private String content;
}
