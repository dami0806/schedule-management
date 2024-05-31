package com.sparta.scheduleapp.dto;

import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); // 초기화
        validator = factory.getValidator();
    }

    @Test
    void getTitle() {
    }

    @Test
    void getDescription() {
    }

    @Test
    @DisplayName("비밀번호 검증 테스트 - 잘못된 이메일 형식")
    void testInvalidAssignee() {
        ScheduleRequestDto requestDto = new ScheduleRequestDto(
                "Title",
                "Description",
                "invalid-email-format",
                "2023-05-19",
                "password");

        Set<ConstraintViolation<ScheduleRequestDto>> violations = validator.validate(requestDto);

        ConstraintViolation<ScheduleRequestDto> violation = violations.iterator().next();
        assertEquals("담당자 작성은 email 형식이어야 합니다.", violation.getMessage());
    }
    @Test
    void getPassword() {

    }

    @Test
    void getDate() {
    }

    @Test
    void setTitle() {
    }

    @Test
    void setDescription() {
    }

    @Test
    void setAssignee() {
    }

    @Test
    void setPassword() {
    }

    @Test
    void setDate() {
    }
}
