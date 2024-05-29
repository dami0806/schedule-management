package com.sparta.scheduleapp.schedule.controller;

import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.schedule.dto.ScheduleResponseDto;
import com.sparta.scheduleapp.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 스케줄 관리 Controller
 */
@RestController
@RequestMapping("/api")
@Tag(name = "ScheduleController", description = "스케줄 관리 API")
@Validated
public class ScheduleController {

    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedules")
    @Operation(summary = "스케줄 추가", description = "새로운 스케줄을 추가합니다.")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        //여기서 ScheduleResponseDto .... = service(dto)받고
        return ResponseEntity.status(HttpStatus.CREATED).body
                (new ScheduleResponseDto(scheduleService.createSchedule(requestDto, userDetails.getUsername())));
    }

    @GetMapping("/schedules")
    @Operation(summary = "스케줄 목록 조회", description = "모든 스케줄 목록을 조회합니다.")
    public List<ScheduleResponseDto> getScheduleList() {
        return scheduleService.getScheduleList().stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedules/{id}")
    @Operation(summary = "스케줄 상세 조회", description = "특정 스케줄의 상세 정보를 조회합니다.")
    public ResponseEntity<ScheduleResponseDto> getDetailSchedule(@PathVariable Long id) {
     //return scheduleService.getDetailSchedule(id);
        return ResponseEntity.ok(new ScheduleResponseDto(scheduleService.getDetailSchedule(id)));
    }

    @PutMapping("/schedules/{id}")
    @Operation(summary = "스케줄 수정", description = "특정 스케줄을 수정합니다.")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto requestDto,@AuthenticationPrincipal UserDetails userDetails) {
     // return scheduleService.updateSchedule(id, requestDto);
        return ResponseEntity.ok(new ScheduleResponseDto(scheduleService.updateSchedule(id, requestDto, userDetails.getUsername())));
    }

    @DeleteMapping("/schedules/{id}")
    @Operation(summary = "스케줄 삭제", description = "특정 스케줄을 삭제합니다.")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        //return scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(scheduleService.deleteSchedule(id,userDetails.getUsername()));
    }

    @PostMapping("/schedules/validatePassword/{id}")
    @Operation(summary = "비밀번호 검증", description = "특정 스케줄의 비밀번호를 검증합니다.")
    public ResponseEntity<Boolean> verifyPassword(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
       //return scheduleService.verifyPassword(id, requestBody.get("password"));
        return ResponseEntity.ok(scheduleService.verifyPassword(id, requestBody.get("password")));
    }
}
