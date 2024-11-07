package org.example.exceptions;

public class InputFileNameException extends RuntimeException {
    public InputFileNameException() {
        super("The name of the file entered from the keyboard is empty!");
    }
}
