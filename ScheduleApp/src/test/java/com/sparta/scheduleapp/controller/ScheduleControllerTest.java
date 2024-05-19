package com.sparta.scheduleapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sparta.scheduleapp.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.dto.ScheduleResponseDto;
import com.sparta.scheduleapp.entity.Schedule;
import com.sparta.scheduleapp.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private WebApplicationContext context;

    private Schedule schedule;
    private ScheduleResponseDto responseDto;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        // 테스트용 스케줄 엔티티 객체 생성
        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setTitle("제목1");
        schedule.setDescription("내용1");
        schedule.setAssignee("test@naver.com");
        schedule.setDate("2024-05-17");
        schedule.setPassword("비번1");

        // 테스트용 스케줄 응답 DTO 객체 생성
        responseDto = new ScheduleResponseDto(schedule);
    }

    // 스케줄 생성 테스트
    @Test
    public void 스케줄생성() throws Exception {

        // 테스트용 스케줄 엔티티 객체 생성
        Schedule schedule = new Schedule();
        schedule.setId(2L);
        schedule.setTitle("Test");
        schedule.setDescription("Test Description");
        schedule.setAssignee("test@naver.com");
        schedule.setDate("2024-05-17");
        schedule.setPassword("password");

        // 테스트용 스케줄 응답 DTO 객체 생성
        ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);

        when(scheduleService.createSchedule(any(ScheduleRequestDto.class)))
                .thenReturn(ResponseEntity.status(201).body(responseDto));

        mockMvc.perform(post("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test\", \"description\": \"Test Description\", \"assignee\": \"test@naver.com\", \"password\": \"password\", \"date\": \"2024-05-17\" }"))
                .andExpect(status().isCreated()) // HTTP 상태 코드 201(CREATED)인지 확인
                .andExpect(jsonPath("$.title", is("Test"))); // 응답의 title이 "Test"인지 확인
    }


    @Test
    void 스케줄리스트() throws Exception {
        //given

        //when
        when(scheduleService.getScheduleList()).thenReturn(Collections.singletonList(responseDto));
        //then
        mockMvc.perform(get("/api/schedule"))
                .andExpect(status().isOk()) // HTTP 상태 코드 200(OK)인지 확인
                .andExpect(jsonPath("$[0].title", is("제목1")));
    }

    @Test
    void 스케줄상세() throws Exception {
        // 서비스의 getDetailSchedule 메서드가 호출되면 responseDto를 반환
        //given

        //when
        when(scheduleService.getDetailSchedule(anyLong())).thenReturn(ResponseEntity.status(200).body(responseDto));

        //then
        mockMvc.perform(get("/api/schedule/1"))
                .andExpect(status().isOk()) // HTTP 상태 코드 200(OK)인지 확인
                .andExpect(jsonPath("$.title", is("제목1")));
    }

    @Test
    void 스케줄수정() throws Exception {
        // 서비스의 updateSchedule 메서드가 호출되면 responseDto를 반환
        //객체 만들어서 수정DTO생성

        //given
        // 수정된 스케줄 응답 DTO 객체 생성
        Schedule updatedSchedule = new Schedule();
        updatedSchedule.setId(1L);
        updatedSchedule.setTitle("수정제목");
        updatedSchedule.setDescription("수정본문");
        updatedSchedule.setAssignee("test@naver.com");
        updatedSchedule.setDate("2024-05-17");
        updatedSchedule.setPassword("수정번호");

        //when
        ScheduleResponseDto updatedResponseDto = new ScheduleResponseDto(updatedSchedule);

        //then
        // 서비스의 updateSchedule 메서드가 호출되면 updatedResponseDto를 반환
        when(scheduleService.updateSchedule(anyLong(), any(ScheduleRequestDto.class)))
                .thenReturn(ResponseEntity.status(200).body(updatedResponseDto));

        // 스케줄 수정 API 호출 및 검증
        mockMvc.perform(put("/api/schedule/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"수정제목\", \"description\": \"수정본문\", \"assignee\": \"test@naver.com\", \"password\": \"수정번호\", \"date\": \"2024-05-17\" }"))
                .andExpect(status().isOk()) // HTTP 상태 코드 200(OK)인지 확인
                .andExpect(jsonPath("$.title", is("수정제목")));
    }

    @Test
    void 스케줄삭제() throws Exception {
        // 서비스의 deleteSchedule 메서드가 호출되면 성공 메시지를 반환
        //when
        when(scheduleService.deleteSchedule(anyLong()))
                .thenReturn(ResponseEntity.ok("success"));

        mockMvc.perform(delete("/api/schedule/1"))
                .andExpect(status().isOk());
    }

    @Test
    void verifyPassword() throws Exception {
        // 서비스의 verifyPassword 메서드가 호출되면 true를 반환
        when(scheduleService.verifyPassword(anyLong(), any(String.class)))
                .thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(post("/api/schedule/validatePassword/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"password\": \"비번1\" }"))
                .andExpect(status().isOk()) // HTTP 상태 코드 200(OK)인지 확인
                .andExpect(jsonPath("$", is(true))); // 응답이 true인지 확인
    }
}