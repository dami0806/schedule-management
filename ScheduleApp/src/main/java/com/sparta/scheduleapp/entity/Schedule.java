package com.sparta.scheduleapp.entity;

import com.sparta.scheduleapp.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "schedule")
public class Schedule {
    //엔티티 -> 데이터베이스에 넘길것
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;
    @Column(name = "description", nullable = false, length = 200)
    private String description;
    @Column(name = "assignee", nullable = false, length = 100)
    private String assignee;
    @Column(name = "date", nullable = false, length = 200)
    private String date;
    @Column(name = "password", nullable = false, length = 200)
    private String password;
    @Column(name = "isDeleted")
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