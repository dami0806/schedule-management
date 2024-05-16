package org.sparta.scheduleapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.scheduleapp.controller.ScheduleController;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;
import org.sparta.scheduleapp.dto.ScheduleResponseDto;
import org.sparta.scheduleapp.entity.Schedule;
import org.sparta.scheduleapp.exception.InvalidPasswordException;
import org.sparta.scheduleapp.exception.ScheduleAlreadyDeletedException;
import org.sparta.scheduleapp.exception.ScheduleNotFoundException;
import org.sparta.scheduleapp.exception.message.ErrorMessage;
import org.sparta.scheduleapp.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

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
        return scheduleRepository.findAll();
    }

    public ResponseEntity<ScheduleResponseDto> getDetailSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id);

        if (schedule == null) {
            log.error("스케줄을 찾을 수 없음, id: {}", id);
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        } else if (schedule.isDeleted()) {
            throw new ScheduleAlreadyDeletedException(ErrorMessage.SCHEDULE_ALREADY_DELETED);
        }
        ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
        return ResponseEntity.ok(responseDto);
    }

    // Schedule 객체를 반환하는 헬퍼 메서드


    public ResponseEntity<Boolean> verifyPassword(Long id, String password) {
        Schedule schedule = scheduleRepository.findById(id);

        if (schedule == null) {
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        }

        if (!schedule.getPassword().equals(password)) {
            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD);
        }
        return ResponseEntity.ok(true);
    }

    public ResponseEntity<String> deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id);
        if (schedule != null) {
            if (schedule.isDeleted()) {
                throw new ScheduleAlreadyDeletedException(ErrorMessage.SCHEDULE_ALREADY_DELETED);
            }
            scheduleRepository.delete(id);
            schedule.setDeleted(true);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else {
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        }
    }

    public ResponseEntity<ScheduleResponseDto> updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id);
        if (schedule != null) {
            scheduleRepository.update(id, requestDto);

            schedule.update(requestDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ScheduleResponseDto(schedule));
        } else {
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        }
    }
}
