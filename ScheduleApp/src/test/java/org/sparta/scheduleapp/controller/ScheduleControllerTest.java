package org.sparta.scheduleapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;
import org.sparta.scheduleapp.dto.ScheduleResponseDto;
import org.sparta.scheduleapp.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class ScheduleControllerTest {

    private ScheduleController scheduleController;
    private ConcurrentHashMap<Long, Schedule> scheduleList;

    @BeforeEach
    void setUp() {
        scheduleController = new ScheduleController();
       // scheduleList = (ConcurrentHashMap<Long, Schedule>) scheduleController.getScheduleList();
        // 리플렉션을 사용으로 scheduleList 필드에 접근
        try {
            Field scheduleListField = ScheduleController.class.getDeclaredField("scheduleList");
            scheduleListField.setAccessible(true);
            scheduleList = (ConcurrentHashMap<Long, Schedule>) scheduleListField.get(scheduleController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void 스케줄생성() {
        // given 주어졌을때
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setTitle("제목");
        scheduleRequestDto.setDescription("글");
        scheduleRequestDto.setAssignee("작성자");
        scheduleRequestDto.setDate("2024-06-01");

        // when 언제
        ResponseEntity<ScheduleResponseDto> response = scheduleController.createSchedule(scheduleRequestDto);

        // then 어떻게 결과가 나왔는 것이 될지
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("제목");
        assertThat(response.getBody().getDescription()).isEqualTo("글");
        assertThat(response.getBody().getAssignee()).isEqualTo("작성자");
        assertThat(response.getBody().getDate()).isEqualTo("2024-06-01");
    }


    @Test
    void 스케줄리스트전체보기() {
        // given
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setTitle("제목");
        scheduleRequestDto.setDescription("글");
        scheduleRequestDto.setAssignee("작성자");
        scheduleRequestDto.setDate("2024-05-16");

        Schedule schedule = new Schedule(scheduleRequestDto);
        schedule.setId(1L);
        schedule.setPassword("1234");

        scheduleList.put(1L, schedule);

        // when
        List<ScheduleResponseDto> responseList = scheduleController.getScheduleList();

        // then
        assertThat(responseList).isNotEmpty();
        assertThat(responseList.get(0).getTitle()).isEqualTo("제목");
        assertThat(responseList.get(0).getDescription()).isEqualTo("글");
        assertThat(responseList.get(0).getAssignee()).isEqualTo("작성자");
        assertThat(responseList.get(0).getDate()).isEqualTo("2024-05-16");


    }

    @Test
    void 상세한스케줄보기() {
        // given 주어졌을때
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setTitle("제목임다");
        scheduleRequestDto.setDescription("글");
        scheduleRequestDto.setAssignee("작성자");
        scheduleRequestDto.setDate("2024-06-01");

        Schedule schedule = new Schedule(scheduleRequestDto);
        schedule.setId(1L);
        schedule.setPassword("1234");

        scheduleList.put(1L, schedule);
        // when 언제
        ResponseEntity<ScheduleResponseDto> response = scheduleController.createSchedule(scheduleRequestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("제목임다");
        assertThat(response.getBody().getDescription()).isEqualTo("글");
        assertThat(response.getBody().getAssignee()).isEqualTo("작성자");
        assertThat(response.getBody().getDate()).isEqualTo("2024-06-01");
    }

    @Test
    void 스케줄수정() {
        // given 주어졌을때
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setTitle("제목임다");
        scheduleRequestDto.setDescription("글");
        scheduleRequestDto.setAssignee("작성자");
        scheduleRequestDto.setDate("2024-06-01");

        Schedule schedule = new Schedule(scheduleRequestDto);
        schedule.setId(1L);
        schedule.setPassword("1234");

        scheduleList.put(1L, schedule);
        ScheduleRequestDto updateRequestDto = new ScheduleRequestDto();
        updateRequestDto.setTitle("제목2");
        updateRequestDto.setDescription("글2");
        updateRequestDto.setAssignee("작성자2");
        updateRequestDto.setDate("2024-06-02");

        // when 언제: 수정하기
        ResponseEntity<ScheduleResponseDto> response = scheduleController.updateSchedule(1L, updateRequestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("제목2");
        assertThat(response.getBody().getDescription()).isEqualTo("글2");
        assertThat(response.getBody().getAssignee()).isEqualTo("작성자2");
        assertThat(response.getBody().getDate()).isEqualTo("2024-06-02");
    }

    @Test
    void 스케줄삭제() {
        // given 주어졌을때
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setTitle("제목임다");
        scheduleRequestDto.setDescription("글");
        scheduleRequestDto.setAssignee("작성자");
        scheduleRequestDto.setDate("2024-06-01");

        Schedule schedule = new Schedule(scheduleRequestDto);
        schedule.setId(1L);
        schedule.setPassword("1234");

        scheduleList.put(1L, schedule);
        // when
        scheduleController.deleteSchedule(1L);
        // then
        assertThat(scheduleList.get(1L)).isNull();
    }

    @Test
    void 비밀번호유효성검사() {
        // given
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setTitle("제목임다");
        scheduleRequestDto.setDescription("글");
        scheduleRequestDto.setAssignee("작성자");
        scheduleRequestDto.setDate("2024-06-01");
        Schedule schedule = new Schedule(scheduleRequestDto);
        schedule.setId(1L);
        schedule.setPassword("1234");

        scheduleList.put(1L, schedule);

        // when
        Map<String, String> requestBody = Map.of("password", "1234");
        ResponseEntity<Boolean> response = scheduleController.verifyPassword(1L, requestBody);

        // then
        assertThat(scheduleController.verifyPassword(1L, requestBody).getBody()).isTrue();
    }
}