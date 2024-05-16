package org.sparta.scheduleapp.controller;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;
import org.sparta.scheduleapp.dto.ScheduleResponseDto;
import org.sparta.scheduleapp.entity.Schedule;
import org.sparta.scheduleapp.exception.InvalidPasswordException;
import org.sparta.scheduleapp.exception.ScheduleAlreadyDeletedException;
import org.sparta.scheduleapp.exception.ScheduleNotFoundException;
import org.sparta.scheduleapp.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
@Tag(name = "ScheduleController", description = "스케줄 관리 API")
@Validated
public class ScheduleController {
    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);
    private final Map<Long, Schedule> scheduleList = new ConcurrentHashMap<>();


    // 스케줄 추가 post (/schedule) 요청데이터: 받을데이터:
    @PostMapping("/schedule")
    @Operation(summary = "스케줄 추가", description = "새로운 스케줄을 추가합니다.")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto) {
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
    @Operation(summary = "스케줄 목록 조회", description = "모든 스케줄 목록을 조회합니다.")
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
    @Operation(summary = "스케줄 상세 조회", description = "특정 스케줄의 상세 정보를 조회합니다.")

    public ResponseEntity<ScheduleResponseDto> getDetailSchedule(@PathVariable Long id) {
        Schedule schedule = scheduleList.get(id);
        if (schedule == null) {
            log.error("스케줄을 찾을 수 없음, id: {}", id);
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        }
        else if (schedule.isDeleted()) {
            throw new ScheduleAlreadyDeletedException(ErrorMessage.SCHEDULE_ALREADY_DELETED);
        }
        ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
        return ResponseEntity.ok(responseDto);

    }
    //수정하기 put
    @PutMapping("/schedule/{id}")
    @Operation(summary = "스케줄 수정", description = "특정 스케줄을 수정합니다.")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleList.get(id);

        if (scheduleList.containsKey(id)) {
            //schedule request로 받은 Dto -> Entity에 받아와서 set하고 ResponseEntity로 반환
            schedule.update(requestDto);

            schedule.setTitle(requestDto.getTitle());
            schedule.setDescription(requestDto.getDescription());
            schedule.setAssignee(requestDto.getAssignee());
            schedule.setDate(requestDto.getDate());

            scheduleList.put(id, schedule);

            return ResponseEntity.status(HttpStatus.OK).body(new ScheduleResponseDto(schedule));
        }else if (schedule.isDeleted()) {
            throw new ScheduleAlreadyDeletedException(ErrorMessage.SCHEDULE_ALREADY_DELETED);
        } else {
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        }
    }

    // 삭제: id 받아와서 list에서 삭제후 성공 실패 반환
    @DeleteMapping("/schedule/{id}")
    @Operation(summary = "스케줄 삭제", description = "특정 스케줄을 삭제합니다.")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        Schedule schedule = scheduleList.get(id);
        if (scheduleList.containsKey(id)) {
            scheduleList.remove(id);
            schedule.setDeleted(true);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } else if (schedule.isDeleted()) {
            throw new ScheduleAlreadyDeletedException(ErrorMessage.SCHEDULE_ALREADY_DELETED);
        } else {
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        }
    }

    //비밀번호 검증 post id받아서 검증후 성공 실패 보이기
    @PostMapping("/schedule/validatePassword/{id}")
    @Operation(summary = "비밀번호 검증", description = "특정 스케줄의 비밀번호를 검증합니다.")
    public ResponseEntity<Boolean> verifyPassword(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        Schedule schedule = scheduleList.get(id);
        String inputPassword = requestBody.get("password");

        if (schedule == null) {
            throw new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND);
        }

        if (!schedule.getPassword().equals(inputPassword)) {
            throw new InvalidPasswordException(ErrorMessage.INVALID_PASSWORD);
        }
        return ResponseEntity.ok(true);
    }
}