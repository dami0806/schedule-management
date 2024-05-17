package org.sparta.scheduleapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;
import org.sparta.scheduleapp.dto.ScheduleResponseDto;
import org.sparta.scheduleapp.entity.Schedule;
import org.sparta.scheduleapp.exception.InvalidPasswordException;
import org.sparta.scheduleapp.exception.ScheduleAlreadyDeletedException;
import org.sparta.scheduleapp.exception.ScheduleNotFoundException;
import org.sparta.scheduleapp.exception.message.ErrorMessage;
import org.sparta.scheduleapp.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {
    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    private Schedule schedule;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // 테스트용 스케줄 엔티티 객체 생성
        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setTitle("제목1");
        schedule.setDescription("내용1");
        schedule.setAssignee("test@naver.com");
        schedule.setDate("2024-05-17");
        schedule.setPassword("비번1");
    }

    @Test
    void 스케줄생성테스트() {
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("생성");
        requestDto.setDescription("생성본문");
        requestDto.setAssignee("test@naver.com");
        requestDto.setDate("2024-05-17");
        requestDto.setPassword("password");

        ResponseEntity<ScheduleResponseDto> response = scheduleService.createSchedule(requestDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("생성", response.getBody().getTitle());
    }

    @Test
    void 스케줄목록조회테스트() {
        // given
        Schedule scheduleEntity = new Schedule();
        scheduleEntity.setId(1L);
        scheduleEntity.setTitle("제목1");
        scheduleEntity.setDescription("내용1");
        scheduleEntity.setAssignee("test@naver.com");
        scheduleEntity.setDate("2024-05-17");
        scheduleEntity.setPassword("비번1");
        ScheduleResponseDto schedule1 = new ScheduleResponseDto(scheduleEntity);

        List<ScheduleResponseDto> mockScheduleList = Arrays.asList(schedule1);
        when(scheduleRepository.findAll()).thenReturn(mockScheduleList);

        // when
        List<ScheduleResponseDto> scheduleList = scheduleService.getScheduleList();

        // then
        assertFalse(scheduleList.isEmpty(), "비어있다");
        assertEquals(1, scheduleList.size(), "크기는 1");
        assertEquals("제목1", scheduleList.get(0).getTitle(), "매치성공");

    }

    @Test
    void 상세조회() {
        when(scheduleRepository.findById(anyLong())).thenReturn(schedule);

        ResponseEntity<ScheduleResponseDto> response = scheduleService.getDetailSchedule(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("제목1", response.getBody().getTitle());
    }

    @Test
    public void 잘못된비밀번호검증테스트() {
        // Mocking: 특정 ID로 스케줄 반환
        when(scheduleRepository.findById(anyLong())).thenReturn(schedule);

        assertThrows(InvalidPasswordException.class, () -> {
            scheduleService.verifyPassword(1L, "잘못된비밀번호");
        });
    }

    @Test
    void deleteSchedule() {
        when(scheduleRepository.findById(anyLong())).thenReturn(schedule);

        ResponseEntity<Boolean> response = scheduleService.verifyPassword(1L, "비번1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void updateSchedule() {
        // Mocking: 특정 ID로 스케줄 반환 및 스케줄 업데이트
        when(scheduleRepository.findById(anyLong())).thenReturn(schedule);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("수정테스트");
        requestDto.setDescription("Updated Test Description");
        requestDto.setAssignee("test@naver.com");
        requestDto.setDate("2024-05-18");
        requestDto.setPassword("password");

        ResponseEntity<ScheduleResponseDto> response = scheduleService.updateSchedule(1L, requestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("수정테스트", response.getBody().getTitle());
    }
}