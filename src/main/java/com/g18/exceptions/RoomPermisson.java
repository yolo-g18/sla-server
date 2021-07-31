package com.g18.exceptions;

public class RoomPermisson extends RuntimeException{
    public RoomPermisson() {

        super("No permission");
    }
}
