//package com.sparta.scheduleapp.service;
//
//import com.sparta.scheduleapp.schedule.dto.ScheduleRequestDto;
//import com.sparta.scheduleapp.schedule.dto.ScheduleResponseDto;
//import com.sparta.scheduleapp.schedule.entity.Schedule;
//import com.sparta.scheduleapp.schedule.repository.ScheduleRepository;
//import com.sparta.scheduleapp.schedule.service.ScheduleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class ScheduleServiceTest {
//    @Mock
//    private ScheduleRepository scheduleRepository;
//
//    @InjectMocks
//    private ScheduleService scheduleService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("스케줄 저장 테스트")
//    void 스케줄저장(){
//    //given
//        ScheduleRequestDto requestDto = new ScheduleRequestDto(
//                "Title",
//                "Description",
//                "dami@naver.com",
//                "password",
//                "2023-05-19");
//
//    //when
//   ScheduleResponseDto response =
//            scheduleService.createSchedule(requestDto);
//
//        //then
//        assertEquals("Title", response.getTitle());
//        assertEquals("Description", response.getDescription());
//        assertEquals("dami@naver.com", response.getAssignee());
//        assertEquals("2023-05-19", response.getDate());
//}
//
//
//    @Test
//    @DisplayName("스케줄 모두불러오기 테스트")
//    void testGetAllSchedules() {
//        // Given
//
//        Schedule schedule1 = new Schedule(
//                1L,
//                "Title1",
//                "Description1",
//                "Assignee1",
//                "2023-05-19",
//                "password",
//                false
//        );
//
//        Schedule schedule2 = new Schedule(
//                2L,
//                "Title2",
//                "Description2",
//                "Assignee2",
//                "2023-05-19",
//                "password",
//                false
//        );
//
//        List<Schedule> schedules = Arrays.asList(schedule1, schedule2);
//        when(scheduleRepository.findAll()).thenReturn(schedules);
//    }
//
//    @Test
//    @DisplayName("스케줄 상세보기 테스트")
//    void testGetDetailSchedule() {
//        // Given
//        Schedule schedule = new Schedule(
//                1L,
//                "Title1",
//                "Description1",
//                "Assignee1",
//                "2023-05-19",
//                "password",
//                false
//        );
//
//        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
//
//        // When
//        ScheduleResponseDto response = scheduleService.getDetailSchedule(1L);
//
//        // Then
//        assertEquals("Title1", response.getTitle());
//        assertEquals("Description1", response.getDescription());
//        assertEquals("Assignee1", response.getAssignee());
//        assertEquals("2023-05-19", response.getDate());
//    }
//
//    @Test
//    @DisplayName("스케줄 삭제 테스트")
//    void testDeleteSchedule() {
//        // Given
//        Schedule schedule = new Schedule(
//                1L,
//                "Title1",
//                "Description1",
//                "Assignee1",
//                "2023-05-19",
//                "password",
//                false
//        );
//
//
//        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
//
//        // When
//        String response = scheduleService.deleteSchedule(1L);
//
//        // Then
//        assertEquals("success", response);
//        verify(scheduleRepository, times(1)).delete(schedule);
//    }
//
//
//
//    @Test
//    @DisplayName("스케줄 업데이트 테스트")
//    void testUpdateSchedule() {
//        // Given
//        Schedule schedule = new Schedule(
//                1L,
//                "Title1",
//                "Description1",
//                "Assignee1",
//                "2023-05-19",
//                "password",
//                false
//        );
//
//        ScheduleRequestDto requestDto = new ScheduleRequestDto(
//                "Title",
//                "Description",
//                "dami@naver.com",
//                "password",
//                "2023-05-19");
//
//        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
//
//        // When
//        ScheduleResponseDto response = scheduleService.updateSchedule(1L, requestDto);
//
//        // Then
//        assertEquals("Title", response.getTitle());
//        assertEquals("Description", response.getDescription());
//        assertEquals("dami@naver.com", response.getAssignee());
//        assertEquals("2023-05-19", response.getDate());
//        verify(scheduleRepository, times(1)).save(schedule);
//    }
//}