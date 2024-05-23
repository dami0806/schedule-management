package com.sparta.scheduleapp.schedule.entity;

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

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true) // 스케줄과 연관된 모든 댓글을 즉시 로딩하고, 스케줄 삭제 시 연관된 댓글도 삭제
    private List<Comment> comments;


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