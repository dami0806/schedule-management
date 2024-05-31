package com.sparta.scheduleapp.file.dto;

import com.sparta.scheduleapp.file.entity.File;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileResponseDto {
    private Long id;
    private String fileName;
    private String fileExtension;
    private long fileSize;
    private String createdDate;

    @Builder
    public FileResponseDto(File file) {
        this.id = file.getId();
        this.fileName = file.getFileName();
        this.fileExtension = file.getFileExtension();
        this.fileSize = file.getFileSize();
        this.createdDate = file.getCreatedDate().toString();
    }
}