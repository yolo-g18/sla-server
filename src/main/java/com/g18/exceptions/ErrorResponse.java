package com.g18.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String message;
}
