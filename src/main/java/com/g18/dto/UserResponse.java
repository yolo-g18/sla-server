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
    private String phone;
    private String email;
    private String address;
    private String schoolName;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant favourTimeFrom;
    private Instant favourTimeTo;
}
