package com.sparta.scheduleapp.exception;

import com.sparta.scheduleapp.exception.message.ErrorMessage;

public class ScheduleAlreadyDeletedException extends RuntimeException {

    public ScheduleAlreadyDeletedException(ErrorMessage message) {
        super(message.getMessage());
    }
}
