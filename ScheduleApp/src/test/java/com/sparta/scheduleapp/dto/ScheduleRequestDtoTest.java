package com.sparta.scheduleapp.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
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
    void getAssignee() {
        ScheduleRequestDto dto = new ScheduleRequestDto();
        dto.setTitle("title");
        dto.setDescription("testDescription");
        dto.setDate("testDate");
        dto.setPassword("testPassword");
        dto.setAssignee("testEmail");

        assertEquals("담당자 작성은  email형식이여야 합니다.", validator.validate(dto).iterator().next().getMessage());

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