package com.g18.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {

        super("User not found");
    }
}
