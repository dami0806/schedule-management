package com.sparta.scheduleapp.comment.entity;

import com.sparta.scheduleapp.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Comment(String content, String userId, Schedule schedule) {
        this.content = content;
        this.userId = userId;
        this.schedule = schedule;
        this.createdAt = LocalDateTime.now();
    }

    // 성능상 객체 생성 보다 업데이트 고려
    public void updateContent(String content) {
        this.content = content;
    }
}