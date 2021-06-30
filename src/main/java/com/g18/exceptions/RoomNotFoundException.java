package com.g18.exceptions;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException() {

        super("Room not found");
    }
}
