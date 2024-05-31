package com.sparta.scheduleapp.schedule.service;

import com.sparta.scheduleapp.exception.*;
import com.sparta.scheduleapp.exception.message.file.FileNotSaveException;
import com.sparta.scheduleapp.exception.message.file.FileTypeNotAllowedException;
import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.file.entity.File;
import com.sparta.scheduleapp.schedule.repository.FileRepository;
import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final FileRepository fileRepository;


    // 스케줄 생성
    public Schedule createSchedule(ScheduleRequestDto requestDto,MultipartFile file, String username){
        File fileEntity = saveFile(file);

        Schedule schedule = Schedule.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .assignee(requestDto.getAssignee())
                .date(requestDto.getDate())
                .password(requestDto.getPassword())
                .creator(username)
                .file(fileEntity)
                .build();

        return scheduleRepository.save(schedule);
    }



    public List<Schedule> getScheduleList() {
        return scheduleRepository.findAll();
    }

    public Schedule getDetailSchedule(Long id) {
        return findSchedule(id);
    }

    public boolean verifyPassword(Long id, String password) {
        Schedule schedule = findSchedule(id);
        if (schedule.getPassword().equals(password)) {
            return true;
        } else {
            log.error("비밀번호 유효성 실패:{}", id);

            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD);
        }
    }

    //일정 수정
    @Transactional
    public Schedule updateSchedule(Long id, ScheduleRequestDto requestDto, MultipartFile file, String username) {
        Schedule schedule = findSchedule(id);
        File savedFile = saveFile(file);

        if (!schedule.getCreator().equals(username)) {
            log.error("권한이 없습니다: {}", username);
            throw new UnauthorizedException("권한이 없습니다.");
        }
        Schedule updatedSchedule = Schedule.builder()
                .id(schedule.getId()) // 기존 id 유지
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .assignee(requestDto.getAssignee())
                .date(requestDto.getDate())
                .password(requestDto.getPassword())
                .creator(schedule.getCreator()) // 기존 작성자 유지
                .file(savedFile)
                .build();
        return scheduleRepository.save(updatedSchedule);
    }

    // 일정 삭제
    public String deleteSchedule(Long id, String username) {
        Schedule schedule = findSchedule(id);
        if (!schedule.getCreator().equals(username)) {
            log.error("권한이 없습니다: {}", username);

            throw new UnauthorizedException("권한이 없습니다.");
        }
        scheduleRepository.delete(schedule);
        return "success";
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> {
            log.error("Schedule not found with id: {}", id);
            return new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        });
    }

    //저장
    private File saveFile(MultipartFile file) {
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
                throw new FileNotSaveException("파일 저장 중 오류 발생"); // 필요에 따라 사용자 정의 예외를 던질 수도 있습니다.
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