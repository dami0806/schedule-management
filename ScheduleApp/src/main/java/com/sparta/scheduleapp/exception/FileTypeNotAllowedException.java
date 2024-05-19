package com.sparta.scheduleapp.exception;

public class FileTypeNotAllowedException extends RuntimeException {

    public FileTypeNotAllowedException(String message) {
        super(message);
    }
}
