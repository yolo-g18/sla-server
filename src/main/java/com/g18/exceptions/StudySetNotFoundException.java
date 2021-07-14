package com.g18.exceptions;

public class StudySetNotFoundException extends RuntimeException{
    public StudySetNotFoundException() {

        super("StudySet not found");
    }
}
