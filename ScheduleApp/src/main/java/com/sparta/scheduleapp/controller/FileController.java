package com.sparta.scheduleapp.controller;

import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.exception.FileTypeNotAllowedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {
    // MacOS의 사용자 홈 디렉토리 아래에 uploads 디렉토리 사용
    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";

    /**
     * 파일 업로드 엔드포인트
     *
     * @param file 업로드할 MultipartFile 객체
     * @return 업로드 성공 또는 실패 메시지
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 업로드된 파일의 원래 이름.
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // 파일 형식이 허용된 형식인지 확인
            if (!isAllowedFileType(fileName)) {
                throw new FileTypeNotAllowedException(ErrorMessage.INVALID_FILETYPE.getMessage());
            }

            // 파일을 저장할 경로를 설정
            Path path = Paths.get(UPLOAD_DIR + fileName);

            // 경로가 존재하지 않으면 디렉토리를 생성
            Files.createDirectories(path.getParent());

            // 파일을 경로에 저장합니다.
            Files.write(path, file.getBytes());

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
    }

    /**
     * 파일 다운로드 엔드포인트
     *
     * @param fileName 다운로드할 파일의 이름
     * @return 파일의 바이트 배열과 HTTP 응답 헤더
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            // 파일을 저장한 경로를 설정
            Path path = Paths.get(UPLOAD_DIR + fileName);
            File file = path.toFile();

            // 파일이 존재하지 않으면 404
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 파일을 읽어 바이트 배열로 변환
            InputStream inputStream = new FileInputStream(file);
            byte[] fileBytes = inputStream.readAllBytes();
            inputStream.close();

            // 다운로드 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);

            // 파일의 바이트 배열,  200 OK 응답을 반환
            return ResponseEntity.ok().headers(headers).body(fileBytes);
        } catch (IOException e) {
            // 파일 읽기 중 예외가 발생하면 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    /**
     * 허용된 파일 형식인지 확인하는 메서드
     *
     * @param fileName 파일 이름
     * @return 파일 형식이 허용되면 true, 그렇지 않으면 false
     */
    private boolean isAllowedFileType(String fileName) {
        // 파일 확장자-> 소문자로 변환
        String fileExtension = StringUtils.getFilenameExtension(fileName).toLowerCase();

        // 허용된 파일 형식인지 확인.
        return fileExtension.equals("jpg") || fileExtension.equals("png") || fileExtension.equals("jpeg");
    }

}
