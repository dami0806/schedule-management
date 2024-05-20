package com.sparta.scheduleapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.sparta.scheduleapp.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.dto.ScheduleResponseDto;
import com.sparta.scheduleapp.entity.Schedule;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.repository.ScheduleRepository;
import org.mockito.junit.jupiter.MockitoExtension;
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
    @DisplayName("스케줄 저장 테스트")
    void 스케줄저장(){
    //given
    ScheduleRequestDto requestDto = new ScheduleRequestDto();
    requestDto.setTitle("Title");
    requestDto.setDescription("Description");
    requestDto.setAssignee("Assignee");
    requestDto.setDate("2023-05-19");
    requestDto.setPassword("password");

    //when
   ScheduleResponseDto response =
            scheduleService.createSchedule(requestDto);

        //then
        assertEquals("Title", response.getTitle());
        assertEquals("Description", response.getDescription());
        assertEquals("Assignee", response.getAssignee());
        assertEquals("2023-05-19", response.getDate());
}


    @Test
    @DisplayName("스케줄 모두불러오기 테스트")
    void testGetAllSchedules() {
        // Given
        Schedule schedule1 = new Schedule();
        schedule1.setId(1L);
        schedule1.setTitle("Title 1");
        schedule1.setDescription("Description 1");
        schedule1.setAssignee("Assignee 1");
        schedule1.setPassword("password");

        Schedule schedule2 = new Schedule();
        schedule2.setId(2L);
        schedule2.setTitle("Title 2");
        schedule2.setDescription("Description 2");
        schedule2.setAssignee("Assignee 2");
        schedule2.setDate("2023-05-20");
        schedule2.setPassword("password");

        List<Schedule> schedules = Arrays.asList(schedule1, schedule2);
        when(scheduleRepository.findAll()).thenReturn(schedules);
    }

    @Test
    @DisplayName("스케줄 상세보기 테스트")
    void testGetDetailSchedule() {
        // Given
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setTitle("Title");
        schedule.setDescription("Description");
        schedule.setAssignee("Assignee");
        schedule.setDate("2023-05-19");
        schedule.setPassword("password");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        // When
        ScheduleResponseDto response = scheduleService.getDetailSchedule(1L);

        // Then
        assertEquals("Title", response.getTitle());
        assertEquals("Description", response.getDescription());
        assertEquals("Assignee", response.getAssignee());
        assertEquals("2023-05-19", response.getDate());
    }

    @Test
    @DisplayName("스케줄 삭제 테스트")
    void testDeleteSchedule() {
        // Given
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setTitle("Title");
        schedule.setDescription("Description");
        schedule.setAssignee("Assignee");
        schedule.setDate("2023-05-19");
        schedule.setPassword("password");


        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        // When
        String response = scheduleService.deleteSchedule(1L);

        // Then
        assertEquals("success", response);
        verify(scheduleRepository, times(1)).delete(schedule);
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
        ScheduleResponseDto response = scheduleService.updateSchedule(1L, requestDto);

        // Then
        assertEquals("New Title", response.getTitle());
        assertEquals("New Description", response.getDescription());
        assertEquals("New Assignee", response.getAssignee());
        assertEquals("2023-05-20", response.getDate());
        verify(scheduleRepository, times(1)).save(existingSchedule);
    }
}