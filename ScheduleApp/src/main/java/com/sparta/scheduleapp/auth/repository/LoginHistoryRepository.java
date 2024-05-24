package com.sparta.scheduleapp.auth.repository;

import com.sparta.scheduleapp.auth.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

}
