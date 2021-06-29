package com.g18.exceptions;

public class RoomException extends RuntimeException{
    public RoomException(String exMessage, Exception exception) { super(exMessage, exception); }

    public RoomException(String exMessage) { super(exMessage); }
}
