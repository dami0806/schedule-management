package com.sparta.scheduleapp.dto;


import com.sparta.scheduleapp.controller.ScheduleController;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.ScheduleNotFoundException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import com.sparta.scheduleapp.entity.Schedule;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String description;
    private String assignee;
    private String date;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.assignee = schedule.getAssignee();
        this.date = schedule.getDate();
    }
}