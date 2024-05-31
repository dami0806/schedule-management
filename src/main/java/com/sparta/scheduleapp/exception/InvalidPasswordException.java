package com.sparta.scheduleapp.exception;

import com.sparta.scheduleapp.exception.message.ErrorMessage;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(ErrorMessage message) {
        super(ErrorMessage.INVALID_PASSWORD.getMessage());
    }
}
