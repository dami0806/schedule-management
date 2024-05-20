package com.sparta.scheduleapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.sparta.scheduleapp.dto.ScheduleRequestDto;
import com.sparta.scheduleapp.dto.ScheduleResponseDto;
import com.sparta.scheduleapp.entity.Schedule;
import com.sparta.scheduleapp.service.ScheduleService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("스케줄 추가 테스트")
    void createSchedule() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Title");
        requestDto.setDescription("Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("2023-05-19");
        requestDto.setPassword("password");

        ScheduleResponseDto responseDto = new ScheduleResponseDto();

        responseDto.setTitle("Title");
        responseDto.setDescription("Description");
        responseDto.setAssignee("dami@naver.com");
        responseDto.setDate("2023-05-19");
        responseDto.setPassword("password");

        when(scheduleService.createSchedule(any(ScheduleRequestDto.class))).thenReturn(ResponseEntity.status(201).body(responseDto));

        // When & Then
        mockMvc.perform(post("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.assignee", is("dami@naver.com")))
                .andExpect(jsonPath("$.date", is("2023-05-19")))
                .andExpect(jsonPath("$.password", is("password")));
    }

    @Test
    @DisplayName("스케줄 목록 조회 테스트")
    void getScheduleList() throws Exception {
        // Given
        ScheduleResponseDto responseDto = new ScheduleResponseDto();
        responseDto.setTitle("Title");
        responseDto.setDescription("Description");
        responseDto.setAssignee("dami@naver.com");
        responseDto.setDate("2023-05-19");
        responseDto.setPassword("password");

        when(scheduleService.getScheduleList()).thenReturn(Collections.singletonList(responseDto));

        // When & Then
        mockMvc.perform(get("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Title")))
                .andExpect(jsonPath("$[0].description", is("Description")))
                .andExpect(jsonPath("$[0].assignee", is("dami@naver.com")))
                .andExpect(jsonPath("$[0].date", is("2023-05-19")))
                .andExpect(jsonPath("$[0].password", is("password")));
    }

    @Test
    @DisplayName("스케줄 상세 조회 테스트")
    void getDetailSchedule() throws Exception {
        // Given
        ScheduleResponseDto responseDto = new ScheduleResponseDto();
        responseDto.setTitle("Title");
        responseDto.setDescription("Description");
        responseDto.setAssignee("dami@naver.com");
        responseDto.setDate("2023-05-19");
        responseDto.setPassword("password");

        when(scheduleService.getDetailSchedule(anyLong())).thenReturn(ResponseEntity.ok(responseDto));

        // When & Then
        mockMvc.perform(get("/api/schedule/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.assignee", is("dami@naver.com")))
                .andExpect(jsonPath("$.date", is("2023-05-19")))
                .andExpect(jsonPath("$.password", is("password")));
    }

    @Test
    @DisplayName("스케줄 수정 테스트")
    void updateSchedule() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setDescription("Updated Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("2023-05-20");
        requestDto.setPassword("updatedpassword");

        ScheduleResponseDto responseDto = new ScheduleResponseDto();
        responseDto.setTitle("Updated Title");
        responseDto.setDescription("Updated Description");
        responseDto.setAssignee("dami@naver.com");
        responseDto.setDate("2023-05-20");
        responseDto.setPassword("updatedpassword");

        when(scheduleService.updateSchedule(anyLong(), any(ScheduleRequestDto.class))).thenReturn(ResponseEntity.ok(responseDto));

        // When & Then
        mockMvc.perform(put("/api/schedule/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.assignee", is("dami@naver.com")))
                .andExpect(jsonPath("$.date", is("2023-05-20")))
                .andExpect(jsonPath("$.password", is("updatedpassword")));
    }

    @Test
    @DisplayName("스케줄 삭제 테스트")
    void deleteSchedule() throws Exception {
        // Given
        when(scheduleService.deleteSchedule(anyLong())).thenReturn(ResponseEntity.ok("success"));

        // When & Then
        mockMvc.perform(delete("/api/schedule/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    @DisplayName("비밀번호 검증 테스트")
    void verifyPassword() throws Exception {
        // Given
        when(scheduleService.verifyPassword(anyLong(), anyString())).thenReturn(ResponseEntity.ok(true));

        // When & Then
        mockMvc.perform(post("/api/schedule/validatePassword/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}