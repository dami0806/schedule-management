package com.sparta.scheduleapp.file.entity;

import com.sparta.scheduleapp.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileExtension;

    @Column(nullable = false)
    private long fileSize;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Lob
    @Column(name = "file_content", columnDefinition = "LONGBLOB")
    private byte[] fileContent;

    // 1:1 스케줄과 매핑 양방향
    @OneToOne(mappedBy = "file")
    private Schedule schedule;

    @Builder
    public File(String fileName, String fileExtension, long fileSize, LocalDateTime createdDate, byte[] fileContent) {
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.createdDate = createdDate;
        this.fileContent = fileContent;
    }
}
