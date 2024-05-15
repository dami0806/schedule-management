package org.sparta.scheduleapp.exception.message;

public enum ErrorMessage {
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    SCHEDULE_NOT_FOUND("존재하지 않는 일정입니다."),
    SCHEDULE_ALREADY_DELETED("이미 삭제된 일정입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
