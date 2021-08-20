package com.g18.dto;

import com.g18.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String authenticationToken;
    private String refreshToken;
    private String expiresAt;
    private UserResponse userResponse;
    private List<String> roles;
}
