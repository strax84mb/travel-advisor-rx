package com.mn.exceptions;

public class ForbidenActionException extends RuntimeException {

    public ForbidenActionException(String message) {
        super(message);
    }
}
