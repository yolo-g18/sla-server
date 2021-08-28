package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponse {
    private Long userId;
    private String username;
    private String avatar;
    private String bio;
    private int numberStudySetOwn;

}
