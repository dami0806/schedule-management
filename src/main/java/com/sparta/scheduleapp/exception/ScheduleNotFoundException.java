package com.sparta.scheduleapp.exception;

import com.sparta.scheduleapp.exception.message.ErrorMessage;

public class ScheduleNotFoundException extends RuntimeException {

    public ScheduleNotFoundException(ErrorMessage message) {
        super(ErrorMessage.SCHEDULE_NOT_FOUND.getMessage());
    }
}
