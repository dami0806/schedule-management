package com.sparta.scheduleapp.schedule.service;

import com.sparta.scheduleapp.exception.*;
import com.sparta.scheduleapp.exception.message.file.FileNotSaveException;
import com.sparta.scheduleapp.exception.message.file.FileTypeNotAllowedException;
import com.sparta.scheduleapp.file.service.FileService;
import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.file.entity.File;
import com.sparta.scheduleapp.schedule.repository.FileRepository;
import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Qualifier("fileServiceImpl")
    private final FileService fileService;

    // 스케줄 생성
    public Schedule createSchedule(ScheduleRequestDto requestDto, MultipartFile file, String username) {
        File fileEntity = fileService.saveFile(file);

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
        File savedFile = fileService.saveFile(file);

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
}