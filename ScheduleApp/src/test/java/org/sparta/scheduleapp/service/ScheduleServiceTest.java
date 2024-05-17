package org.sparta.scheduleapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sparta.scheduleapp.entity.Schedule;
import org.sparta.scheduleapp.repository.ScheduleRepository;

import static org.junit.jupiter.api.Assertions.*;

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
        schedule.setTitle("Test");
        schedule.setDescription("Test Description");
        schedule.setAssignee("test@naver.com");
        schedule.setDate("2024-05-17");
        schedule.setPassword("password");
    }

    @Test
    void 스케줄생성테스트() {

    }

    @Test
    void getScheduleList() {
    }

    @Test
    void getDetailSchedule() {
    }

    @Test
    void verifyPassword() {
    }

    @Test
    void deleteSchedule() {
    }

    @Test
    void updateSchedule() {
    }
}