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

    // Entity -> Dto 여기서 스케줄에 dto넣기
    //그럼 여기서 ScheduleResponseDto로 넘기고
    //public ResponseEntity<ScheduleResponseDto> createSchedule(ScheduleRequestDto requestDto) {
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
            //Dto -> Entity 넣고 저장
            Schedule schedule = new Schedule();
            schedule.setTitle(requestDto.getTitle());
            schedule.setDescription(requestDto.getDescription());
            schedule.setAssignee(requestDto.getAssignee());
            schedule.setDate(requestDto.getDate());
            schedule.setPassword(requestDto.getPassword());

            scheduleRepository.save(schedule);

            //Entity -> Dto  ResponseEntity로 반환
            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            return responseDto;
    }

    public List<ScheduleResponseDto> getScheduleList() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());

    }

    //public ResponseEntity<ScheduleResponseDto> getDetailSchedule(Long id) {
    public ScheduleResponseDto getDetailSchedule(Long id) {

            Schedule schedule = findSchedule(id);
            return new ScheduleResponseDto(schedule);
    }


    // Schedule 객체를 반환하는 헬퍼 메서드

    public boolean verifyPassword(Long id, String password) {
        Schedule schedule = findSchedule(id);
        if (schedule.getPassword().equals(password)) {
            return true;
        } else {
            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD);
        }
    }

    //public ResponseEntity<String> deleteSchedule(Long id) {
    public String deleteSchedule(Long id) {
        Schedule schedule = findSchedule(id);

        scheduleRepository.delete(schedule);
        return "success";

    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {

            Schedule schedule = findSchedule(id);
            schedule.setTitle(requestDto.getTitle());
            schedule.setDescription(requestDto.getDescription());
            schedule.setAssignee(requestDto.getAssignee());
            schedule.setDate(requestDto.getDate());
            scheduleRepository.save(schedule);

           // schedule.update(requestDto);
            return new ScheduleResponseDto(schedule);
    }


    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND));
    }
}
