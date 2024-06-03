package com.sparta.scheduleapp.file.service;

import com.sparta.scheduleapp.exception.message.file.FileNotSaveException;
import com.sparta.scheduleapp.exception.message.file.FileTypeNotAllowedException;
import com.sparta.scheduleapp.file.entity.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class FileServiceImpl implements FileService{
    @Override
    //저장
    public File saveFile(MultipartFile file) {
        // 파일이 null인지, 있는데 빈건지
        if(file != null && !file.isEmpty()) {
            try {
                validateFile(file);
                return File.builder()
                        .fileName(file.getOriginalFilename())
                        .fileExtension(getFileExtension(file.getOriginalFilename()))
                        .fileSize(file.getSize())
                        .createdDate(LocalDateTime.now())
                        .fileContent(file.getBytes())
                        .build();
            }catch (IOException e) {
                log.error("파일 저장 중 오류 발생: {}", e.getMessage());
                throw new FileNotSaveException("파일 저장 중 오류 발생");
            }
        }
        return null;
    }
    // 파일 형식 용량 체크
    private void validateFile(MultipartFile file) {
        String fileType = getFileExtension(file.getOriginalFilename());
        if (!fileType.equalsIgnoreCase("png") && !fileType.equalsIgnoreCase("jpg") && !fileType.equalsIgnoreCase("jpeg")) {
            throw new FileTypeNotAllowedException("파일 형식은 png, jpg, jpeg로 구성됩니다.");
        }
        if(file.getSize() > 5*1024*1024) {
            throw new FileNotSaveException("파일 크기은 최대 5MB 까지만 가능합니다.");
        }
    }

    // 파일 확장자 가져오기
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
