package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long _id;
    private String username;
    private String firstname;
    private String lastname;
    private String avatar;
    private String job;
    private String email;
    private String address;
    private String schoolName;
    private String major;
    private String bio;
    private String createdAt;
    private String updatedAt;
    private String favourTimeFrom;
    private String favourTimeTo;
}
