package com.g18.exceptions;

import com.g18.model.ApiError;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.*;
@Slf4j
@EnableWebMvc
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("message","The URL you have reached is not in service at this time.");
        return new ResponseEntity<Object>(responseBody,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleAccountException(AccountException ex, WebRequest req) {
        final ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT.value());
        apiError.setMessage(ex.getMessage());
        logger.info(ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(RoomException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleRoomException(AccountException ex, WebRequest req) {
        final ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT.value());
        apiError.setMessage(ex.getMessage());
        logger.info(ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest req) {
        final ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setMessage(ex.getMessage());
        logger.info(ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handlerException(Exception ex, WebRequest req) {
        final ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setMessage(ex.getMessage());
        logger.info(ex.getMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Object> handleCityNotFoundException(
            RoomNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Room not found");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNodataFoundException(
            NoDataFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "No data found");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }





}