package com.sparta.scheduleapp.schedule.entity;

import com.sparta.scheduleapp.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String creator; // 스케줄 생성자 정보


    public Schedule(String title, String description, String assignee, String date, String password, String creator) {
        this.init(title, description, assignee, date, password, creator);
    }

    public void update(String title, String description, String assignee, String date, String password) {
        this.init(title, description, assignee, date, password, this.creator);
       //new Schedule(title, description, assignee, date, password, this.creator);
    }

    public void init(String title, String description, String assignee, String date, String password, String creator) {
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.date = date;
        this.password = password;
        this.creator = creator;
    }
}