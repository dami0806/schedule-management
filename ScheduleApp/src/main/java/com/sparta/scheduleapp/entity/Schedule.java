package com.sparta.scheduleapp.entity;

import com.sparta.scheduleapp.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, length = 100)
    private String assignee;

    @Column(nullable = false, length = 200)
    private String date;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public Schedule(String title, String description, String assignee, String date, String password) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.date = date;
        this.password = password;
    }

    public void update(String title, String description, String assignee, String date, String password) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.date = date;
        this.password = password;
    }
}