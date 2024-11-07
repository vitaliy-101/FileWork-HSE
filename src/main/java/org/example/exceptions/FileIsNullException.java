package org.example.exceptions;

public class FileIsNullException extends NullPointerException {
    public FileIsNullException() {
        super("Couldn't find the file by name!");
    }
}
