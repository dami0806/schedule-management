package com.sparta.scheduleapp.schedule.dto;


import com.sparta.scheduleapp.comment.dto.CommentResponseDto;
import lombok.Getter;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<CommentResponseDto> comments;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.assignee = schedule.getAssignee();
        this.date = schedule.getDate();
        this.comments = schedule.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}