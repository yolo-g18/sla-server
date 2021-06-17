package com.g18.exceptions;

public class SLAException extends RuntimeException{
    public SLAException(String exMessage, Exception exception) { super(exMessage, exception); }

    public SLAException(String exMessage) { super(exMessage); }
}
