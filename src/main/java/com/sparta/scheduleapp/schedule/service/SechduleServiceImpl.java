package com.sparta.scheduleapp.schedule.service;

import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.ScheduleNotFoundException;
import com.sparta.scheduleapp.exception.UnauthorizedException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.file.entity.File;
import com.sparta.scheduleapp.file.service.FileService;
import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SechduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Qualifier("fileServiceImpl")
    private final FileService fileService;

    // 스케줄 생성
    @Override
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

    @Override
    public List<Schedule> getScheduleList() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule getDetailSchedule(Long id) {
        return findScheduleById(id);
    }

    @Override
    public boolean verifyPassword(Long id, String password) {
        Schedule schedule = findScheduleById(id);
        if (schedule.getPassword().equals(password)) {
            return true;
        } else {
            log.error("비밀번호 유효성 실패:{}", id);

            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD);
        }
    }

    //일정 수정
    @Transactional
    @Override
    public Schedule updateSchedule(Long id, ScheduleRequestDto requestDto, MultipartFile file, String username) {
        Schedule schedule = findScheduleById(id);
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
    @Override
    public String deleteSchedule(Long id, String username) {
        Schedule schedule = findScheduleById(id);
        if (!schedule.getCreator().equals(username)) {
            log.error("권한이 없습니다: {}", username);

            throw new UnauthorizedException("권한이 없습니다.");
        }
        scheduleRepository.delete(schedule);
        return "success";
    }

    @Override
    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> {
            log.error("Schedule not found with id: {}", id);
            return new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        });
    }
}
