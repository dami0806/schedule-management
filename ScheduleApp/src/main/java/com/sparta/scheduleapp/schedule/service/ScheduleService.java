package com.sparta.scheduleapp.schedule.service;

import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.ScheduleNotFoundException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sparta.scheduleapp.schedule.controller.ScheduleController;
import com.sparta.scheduleapp.schedule.entity.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleRepository scheduleRepository;

    // 스케줄 생성
    public Schedule createSchedule(ScheduleRequestDto requestDto, String username) {
        Schedule schedule = new Schedule(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getAssignee(),
                requestDto.getDate(),
                requestDto.getPassword(),
                username
        );

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getScheduleList() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스케줄을 찾을 수 없습니다.")); // 스케줄 존재 여부 확인
    }


    public Schedule getDetailSchedule(Long id) {
        return findSchedule(id);
    }

    public boolean verifyPassword(Long id, String password) {
        Schedule schedule = findSchedule(id);
        if (schedule.getPassword().equals(password)) {
            return true;
        } else {
            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD);
        }
    }

    // 일정 삭제
    public String deleteSchedule(Long id,String username) {
        Schedule schedule = findSchedule(id);
        if (!schedule.getCreator().equals(username)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
            scheduleRepository.delete(schedule);
        return "success";
    }

    @Transactional
    public Schedule updateSchedule(Long id, ScheduleRequestDto requestDto,String username) {
        Schedule schedule = findSchedule(id);
        if (!schedule.getCreator().equals(username)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        schedule.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getAssignee(), requestDto.getDate(), requestDto.getPassword());
        return scheduleRepository.save(schedule);
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND));
    }
}