package com.sparta.scheduleapp.service;

import com.sparta.scheduleapp.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.dto.ScheduleResponseDto;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.ScheduleNotFoundException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sparta.scheduleapp.controller.ScheduleController;
import com.sparta.scheduleapp.entity.Schedule;
import com.sparta.scheduleapp.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleRepository scheduleRepository;

    public Schedule createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getAssignee(),
                requestDto.getDate(),
                requestDto.getPassword()
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

    public String deleteSchedule(Long id) {
        Schedule schedule = findSchedule(id);
        scheduleRepository.delete(schedule);
        return "success";
    }

    @Transactional
    public Schedule updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = findSchedule(id);
        schedule.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getAssignee(), requestDto.getDate(), requestDto.getPassword());
        return scheduleRepository.save(schedule);
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND));
    }
}