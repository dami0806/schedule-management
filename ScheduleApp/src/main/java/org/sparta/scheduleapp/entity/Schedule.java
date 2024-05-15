package org.sparta.scheduleapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    //엔티티 -> 데이터베이스에 넘길것
    private Long id;
    private String title;
    private String description;
    private String assignee;
    private String date;
    private String password;
    private boolean isDeleted = false;

    public Schedule(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.assignee = requestDto.getAssignee();
        this.date = requestDto.getDate();
        this.password = requestDto.getPassword();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.assignee = requestDto.getAssignee();
        this.date = requestDto.getDate();
    }
}

