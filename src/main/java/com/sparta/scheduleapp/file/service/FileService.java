package com.sparta.scheduleapp.file.service;

import com.sparta.scheduleapp.file.entity.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * file service
 */
public interface FileService {
    File saveFile(MultipartFile file);
}
