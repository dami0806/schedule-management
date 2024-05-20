package com.sparta.scheduleapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.sparta.scheduleapp.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.dto.ScheduleResponseDto;
import com.sparta.scheduleapp.entity.Schedule;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("스케줄 업데이트 테스트")
    void testUpdateSchedule() {
        // Given
        Schedule existingSchedule = new Schedule();
        existingSchedule.setId(1L);
        existingSchedule.setTitle("Old Title");
        existingSchedule.setDescription("Old Description");
        existingSchedule.setAssignee("Old Assignee");
        existingSchedule.setDate("2023-05-19");
        existingSchedule.setPassword("password");

        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("New Title");
        requestDto.setDescription("New Description");
        requestDto.setAssignee("New Assignee");
        requestDto.setDate("2023-05-20");
        requestDto.setPassword("newpassword");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(existingSchedule));

        // When
        ResponseEntity<ScheduleResponseDto> response = scheduleService.updateSchedule(1L, requestDto);

        // Then
        assertEquals("New Title", response.getBody().getTitle());
        assertEquals("New Description", response.getBody().getDescription());
        assertEquals("New Assignee", response.getBody().getAssignee());
        assertEquals("2023-05-20", response.getBody().getDate());
    }
}