package com.mn.exceptions;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String username) {
        super(String.format("Username %s is already taken by another user", username));
    }
}
