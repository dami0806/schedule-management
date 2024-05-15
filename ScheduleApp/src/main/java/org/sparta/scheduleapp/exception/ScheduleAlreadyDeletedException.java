package org.sparta.scheduleapp.exception;

import org.sparta.scheduleapp.exception.message.ErrorMessage;

public class ScheduleAlreadyDeletedException extends RuntimeException {

    public ScheduleAlreadyDeletedException(ErrorMessage message) {
        super(message.getMessage());
    }
}
