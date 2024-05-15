package org.sparta.scheduleapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;
import org.sparta.scheduleapp.dto.ScheduleResponseDto;
import org.sparta.scheduleapp.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final Map<Long, Schedule> scheduleList = new ConcurrentHashMap<>();


    // 스케줄 추가 post (/schedule) 요청데이터: 받을데이터: 바디로 데이터 받아야합
    @PostMapping("/schedule")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        // 1. 사용자 입력 ->DTO -> ENTITY -> 데이터 넣고 -> DTO -> ResponseEntity(DTO)로 반환
        try {
            log.info("Received schedule: {}", requestDto);
            Schedule schedule = new Schedule(requestDto);

            Long maxId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;
            schedule.setId(maxId);
            scheduleList.put(schedule.getId(), schedule);

            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            // log.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 스케줄 리스트 보기 List <ScheduleResponseDto> 받아서 프론트에서 필요한거 호출
    @GetMapping("/schedule")
    public List<ScheduleResponseDto> getScheduleList() {
        log.info("scheduleList: " + scheduleList.values());
        List<ScheduleResponseDto> scheduleResponseDtoList =
                scheduleList.values().stream()
                        .map(schedule -> new ScheduleResponseDto(schedule))
                        .toList();
        return scheduleResponseDtoList;
    }

    // 상세보기 get
    @GetMapping("/schedule/{id}")
    public ResponseEntity<ScheduleResponseDto> getDetailSchedule(@PathVariable Long id) {
        Schedule schedule = scheduleList.get(id);
        if (schedule != null) {
            ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //수정하기 put
    @PutMapping("/schedule/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        if (scheduleList.containsKey(id)) {
            //schedule request로 받은 Dto -> Entity에 받아와서 set하고 ResponseEntity로 반환
            Schedule schedule = scheduleList.get(id);
            schedule.update(requestDto);

            schedule.setTitle(requestDto.getTitle());
            schedule.setDescription(requestDto.getDescription());
            schedule.setAssignee(requestDto.getAssignee());
            schedule.setDate(requestDto.getDate());

            scheduleList.put(id, schedule);

            return ResponseEntity.status(HttpStatus.OK).body(new ScheduleResponseDto(schedule));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 삭제: id 받아와서 list에서 삭제후 성공 실패 반환
    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        if (scheduleList.containsKey(id)) {
            scheduleList.remove(id);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //비밀번호 검증 post id받아서 검증후 성공 실패 보이기
    @PostMapping("/schedule/validatePassword/{id}")
    public ResponseEntity<Boolean> verifyPassword(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            String inputPassword = requestBody.get("password");
            Schedule schedule = scheduleList.get(id);
            if (schedule != null && schedule.getPassword().equals(inputPassword)) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.ok(false);
        } catch (Exception e) {
            log.error("Error verifying password: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    }