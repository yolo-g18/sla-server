package com.g18.exceptions;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException() {

        super("Member not found");
    }
}
