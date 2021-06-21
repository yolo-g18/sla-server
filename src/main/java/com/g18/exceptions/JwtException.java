package com.g18.exceptions;

public class JwtException extends RuntimeException{
    public JwtException(String exMessage, Exception exception) { super(exMessage, exception); }

    public JwtException(String exMessage) { super(exMessage); }
}
