package com.sparta.scheduleapp.schedule.service;

import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.ScheduleNotFoundException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sparta.scheduleapp.schedule.controller.ScheduleController;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    // 스케줄 생성
    public Schedule createSchedule(ScheduleRequestDto requestDto, String username) {
        Schedule schedule = Schedule.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .assignee(requestDto.getAssignee())
                .date(requestDto.getDate())
                .password(requestDto.getPassword())
                .creator(username)
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
    public Schedule updateSchedule(Long id, ScheduleRequestDto requestDto,String username) {
        Schedule schedule = findSchedule(id);
        if (!schedule.getCreator().equals(username)) {
            log.error("권한이 없습니다: {}", username);
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        Schedule updatedSchedule = Schedule.builder()
                .id(schedule.getId()) // 기존 id 유지
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .assignee(requestDto.getAssignee())
                .date(requestDto.getDate())
                .password(requestDto.getPassword())
                .creator(schedule.getCreator()) // 기존 작성자 유지
                .build();
        return scheduleRepository.save(updatedSchedule);
    }

    // 일정 삭제
    public String deleteSchedule(Long id,String username) {
        Schedule schedule = findSchedule(id);
        if (!schedule.getCreator().equals(username)) {
            log.error("권한이 없습니다: {}", username);

            throw new IllegalArgumentException("권한이 없습니다.");
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