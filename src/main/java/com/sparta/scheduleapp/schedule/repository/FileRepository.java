package com.sparta.scheduleapp.schedule.repository;

import com.sparta.scheduleapp.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
