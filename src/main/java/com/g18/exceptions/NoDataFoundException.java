package com.g18.exceptions;

public class NoDataFoundException extends RuntimeException{
    public NoDataFoundException() {

        super("No data found");
    }
}
