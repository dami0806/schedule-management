package com.sparta.scheduleapp.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
사용자 이름 (username)
로그인 시도, 성공, 실패 같은 액션 (action)
해당 액션이 발생한 시간 (timestamp)
 */
@Entity
@NoArgsConstructor
@Getter
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginAction action;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public LoginHistory(String username, LoginAction action, LocalDateTime timestamp) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

}
