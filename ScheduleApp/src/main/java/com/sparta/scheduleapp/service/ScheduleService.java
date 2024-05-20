package com.sparta.scheduleapp.service;

import com.sparta.scheduleapp.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.dto.ScheduleResponseDto;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.ScheduleAlreadyDeletedException;
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

    public ResponseEntity<ScheduleResponseDto> createSchedule(ScheduleRequestDto requestDto) {
        try {
            log.info("Received schedule: {}", requestDto);
            Schedule schedule = new Schedule(requestDto);
            Schedule savedSchedule = scheduleRepository.save(schedule);

            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            log.error("Error creating schedule", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    public List<ScheduleResponseDto> getScheduleList() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());

    }

    public ResponseEntity<ScheduleResponseDto> getDetailSchedule(Long id) {
            Schedule schedule = findSchedule(id);
            return ResponseEntity.ok(new ScheduleResponseDto(schedule));
    }


    // Schedule 객체를 반환하는 헬퍼 메서드


    public ResponseEntity<Boolean> verifyPassword(Long id, String password) {
        Schedule schedule = findSchedule(id);
        if (schedule.getPassword().equals(password)) {
            return ResponseEntity.ok(true);
        } else {
            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD);
        }
    }

    public ResponseEntity<String> deleteSchedule(Long id) {
        Schedule schedule = findSchedule(id);

        scheduleRepository.delete(schedule);
        return ResponseEntity.ok("success");

    }

    @Transactional
    public ResponseEntity<ScheduleResponseDto> updateSchedule(Long id, ScheduleRequestDto requestDto) {
        try {
            Schedule schedule = findSchedule(id);
            schedule.update(requestDto);
            return ResponseEntity.ok(new ScheduleResponseDto(schedule));
        } catch (Exception e) {
            log.error("Error updating schedule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND));
    }
}
