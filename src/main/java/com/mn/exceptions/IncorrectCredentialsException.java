package com.mn.exceptions;

public class IncorrectCredentialsException extends RuntimeException {

    public IncorrectCredentialsException() {
        super("Provided credentials are incorrect!");
    }
}
