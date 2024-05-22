package com.sparta.scheduleapp.auth.repository;

import com.sparta.scheduleapp.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername (String username);

    Optional<User> findByEmail (String email);
}
