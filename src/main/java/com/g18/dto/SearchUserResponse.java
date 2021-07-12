package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponse {
    private Long id;
    private String username;
    private int numberStudySetOwn;
    private String avatar;
}
