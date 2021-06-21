package com.g18.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private Map<String, String> errors;

    public ApiError(final int status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        HashMap<String, String> map = new HashMap<>();
        map.put("n", error);
        this.errors = map;
    }
}
