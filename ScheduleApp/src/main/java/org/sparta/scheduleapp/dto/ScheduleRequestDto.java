package org.sparta.scheduleapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleRequestDto {
    private String title;
    private String description;
    private String assignee;
    private String password;
    private String date;
}
