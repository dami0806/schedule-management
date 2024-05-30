package com.sparta.scheduleapp.exception.message.file;

public class FileSizeExceededException extends FileNotSaveException {
    public FileSizeExceededException(String message) {
        super(message);
    }
}
