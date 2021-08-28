package com.g18.exceptions;

public class FolderNotFoundException extends RuntimeException{
    public FolderNotFoundException() {

        super("Folder not found");
    }
}
