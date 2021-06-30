package com.g18.exceptions;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException(Long id) {

        super(String.format("Room with Id %d not found", id));
    }
}
