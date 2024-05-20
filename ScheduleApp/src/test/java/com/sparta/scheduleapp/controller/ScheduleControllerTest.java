package com.sparta.scheduleapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.scheduleapp.exception.InvalidPasswordException;
import com.sparta.scheduleapp.exception.ScheduleAlreadyDeletedException;
import com.sparta.scheduleapp.exception.ScheduleNotFoundException;
import com.sparta.scheduleapp.exception.message.ErrorMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
// MARK: - Create Schedule

    @Test
    @DisplayName("스케줄 추가 테스트 - 성공")
    void 스케줄생성성공() throws Exception {
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

        when(scheduleService.createSchedule(any(ScheduleRequestDto.class))).thenReturn(responseDto);

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
    @DisplayName("스케줄 추가 테스트 - 실패(작성자 email형식 아님)")
    void 스케줄생성실패_작성자email형식아님() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Title");
        requestDto.setDescription("Description");
        requestDto.setAssignee("이메일 형식 아님");
        requestDto.setDate("2023-05-19");
        requestDto.setPassword("password");

        // 작성자가 이메일형식이 아님 에러
        // When & Then
        mockMvc.perform(post("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.assignee", containsString("담당자 작성은  email형식이여야 합니다.")));
    }

    @Test
    @DisplayName("스케줄 추가 테스트 - 실패(작성자 비어 있음)")
    void 스케줄생성실패_작성자비어있음() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("title");
        requestDto.setDescription("Description");
        requestDto.setAssignee("");
        requestDto.setDate("2023-05-19");
        requestDto.setPassword("password");
        // 제목 비움
        // When & Then
        mockMvc.perform(post("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.assignee", containsString("담당자 작성은 필수입니다.")));
    }


    @Test
    @DisplayName("스케줄 추가 테스트 - 실패(제목 비어 있음)")
    void 스케줄생성실패_제목비어있음() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("");
        requestDto.setDescription("Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("2023-05-19");
        requestDto.setPassword("password");
        // 제목 비움
        // When & Then
        mockMvc.perform(post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.title", containsString("할일 제목 입력은 필수입니다.")));
    }
    @Test
    @DisplayName("스케줄 추가 테스트 - 실패(제목 최대길이 초과)")
    void 스케줄생성실패_제목최대길이초과() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("a".repeat(201));
        requestDto.setDescription("Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("2023-05-19");
        requestDto.setPassword("password");
        // 제목 비움
        // When & Then
        mockMvc.perform(post("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.title", containsString("할일 제목은 최대 200자입니다.")));
    }
    //비밀번호 필수
    @Test
    @DisplayName("스케줄 추가 테스트 - 실패(비밀번호 비어있음)")
    void 스케줄생성실패_비밀번호필수() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("title");
        requestDto.setDescription("Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("2023-05-19");
        requestDto.setPassword("");
        // 제목 비움
        // When & Then
        mockMvc.perform(post("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.password", containsString( "비밀번호 입력은 필수입니다.")));
    }
    // 날짜입력 필수
    @Test
    @DisplayName("스케줄 추가 테스트 - 실패(날짜 비어있음)")
    void 스케줄생성실패_날짜필수() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("title");
        requestDto.setDescription("Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("");
        requestDto.setPassword("비밀");
        // 제목 비움
        // When & Then
        mockMvc.perform(post("/api/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.date", containsString( "날짜 입력은 필수입니다.")));
    }


// MAKR: - 조회
    @Test
    @DisplayName("스케줄 목록 조회 테스트")
    void getScheduleList() throws Exception {
        // Given
        ScheduleResponseDto responseDto = new ScheduleResponseDto();
        responseDto.setTitle("Title");
        responseDto.setDescription("Description");
        responseDto.setAssignee("dami@naver.com");
        responseDto.setDate("2023-05-19");

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

        when(scheduleService.getDetailSchedule(anyLong())).thenReturn(responseDto);

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
    @DisplayName("스케줄 수정 테스트 - 성공")
    void 스케줄수정성공() throws Exception {
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

        when(scheduleService.updateSchedule(anyLong(), any(ScheduleRequestDto.class))).thenReturn(responseDto);

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
    @DisplayName("스케줄 수정 테스트 - 실패 (존재하지 않는 ID)")
    void updateSchedule_실패_존재하지않는ID() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setDescription("Updated Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("2023-05-20");
        requestDto.setPassword("updatedpassword");

        when(scheduleService.updateSchedule(anyLong(), any(ScheduleRequestDto.class)))
                .thenThrow(new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND));

        // When & Then
        mockMvc.perform(put("/api/schedule/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ScheduleNotFoundException))
                .andExpect(jsonPath("$.message", containsString(ErrorMessage.SCHEDULE_NOT_FOUND.getMessage())));
    }

    @Test
    @DisplayName("스케줄 수정 테스트 - 실패 (삭제된 ID)")
    void updateSchedule_실패_삭제된ID() throws Exception {
        // Given
        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setDescription("Updated Description");
        requestDto.setAssignee("dami@naver.com");
        requestDto.setDate("2023-05-20");
        requestDto.setPassword("updatedpassword");

        when(scheduleService.updateSchedule(anyLong(), any(ScheduleRequestDto.class)))
                .thenThrow(new ScheduleAlreadyDeletedException(ErrorMessage.SCHEDULE_ALREADY_DELETED));

        // When & Then
        mockMvc.perform(put("/api/schedule/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isGone())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ScheduleAlreadyDeletedException))
                .andExpect(jsonPath("$.message", containsString(ErrorMessage.SCHEDULE_ALREADY_DELETED.getMessage())));
    }

    @Test
    @DisplayName("스케줄 삭제 테스트 -성공")
    void 스케줄삭제성공() throws Exception {
        // Given
        when(scheduleService.deleteSchedule(anyLong())).thenReturn("success");

        // When & Then
        mockMvc.perform(delete("/api/schedule/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    @DisplayName("스케줄 삭제 테스트 - 실패 (존재하지 않는 ID)")
    void 스케줄삭제_실패_존재하지않는ID() throws Exception {

        when(scheduleService.deleteSchedule(anyLong()))
                .thenThrow(new ScheduleNotFoundException(ErrorMessage.SCHEDULE_NOT_FOUND));

        // When & Then
        mockMvc.perform(delete("/api/schedule/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ScheduleNotFoundException))
                .andExpect(jsonPath("$.message", containsString(ErrorMessage.SCHEDULE_NOT_FOUND.getMessage())));
    }
    @Test
    @DisplayName("스케줄 삭제 테스트 - 실패 (삭제된 ID)")
    void 스케줄삭제_실패_삭제된ID() throws Exception {
        // Given

        when(scheduleService.deleteSchedule(anyLong()))
                .thenThrow(new ScheduleAlreadyDeletedException(ErrorMessage.SCHEDULE_ALREADY_DELETED));

        // When & Then
        mockMvc.perform(delete("/api/schedule/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGone())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ScheduleAlreadyDeletedException))
                .andExpect(jsonPath("$.message", containsString(ErrorMessage.SCHEDULE_ALREADY_DELETED.getMessage())));
    }

    @Test
    @DisplayName("비밀번호 검증 테스트")
    void verifyPassword() throws Exception {
        // Given
        when(scheduleService.verifyPassword(anyLong(), anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/schedule/validatePassword/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}