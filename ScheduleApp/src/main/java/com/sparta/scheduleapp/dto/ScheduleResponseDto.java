package com.sparta.scheduleapp.dto;

import lombok.Getter;
import com.sparta.scheduleapp.entity.Schedule;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String description;
    private String assignee;
    private String date;
    private String password;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.assignee = schedule.getAssignee();
        this.date = schedule.getDate();
        this.password = schedule.getPassword();
    }
}
