package org.sparta.scheduleapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;
import org.sparta.scheduleapp.dto.ScheduleResponseDto;
import org.sparta.scheduleapp.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Strictness 붙여야함
class ScheduleRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ScheduleRepository scheduleRepository;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setTitle("제목");
        schedule.setDescription("본문");
        schedule.setAssignee("dami@example.com");
        schedule.setDate("2024-06-01");
        schedule.setPassword("비밀");
        schedule.setDeleted(false);
    }

    @Test
    void save() throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        Connection mockConnection = mock(Connection.class);

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Collections.singletonMap("GENERATED_KEY", 1L));
            return 1;
        });

        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);

        Schedule savedSchedule = scheduleRepository.save(schedule);
        assertEquals(1L, savedSchedule.getId());
    }

    @Test
    void findAll() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Collections.singletonList(new ScheduleResponseDto(schedule)));

        List<ScheduleResponseDto> schedules = scheduleRepository.findAll();
        assertFalse(schedules.isEmpty());
        assertEquals(1, schedules.size());
        assertEquals("제목", schedules.get(0).getTitle());
    }

    @Test
    void findById() {
        when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class), anyLong())).thenAnswer(invocation -> {
            ResultSetExtractor<Schedule> extractor = invocation.getArgument(1);
            ResultSet rs = mock(ResultSet.class);
            when(rs.next()).thenReturn(true).thenReturn(false);
            when(rs.getLong("id")).thenReturn(1L);
            when(rs.getString("title")).thenReturn("제목");
            when(rs.getString("description")).thenReturn("본문");
            when(rs.getString("assignee")).thenReturn("dami@example.com");
            when(rs.getString("date")).thenReturn("2024-06-01");
            when(rs.getString("password")).thenReturn("비밀");
            when(rs.getBoolean("is_deleted")).thenReturn(false);
            return extractor.extractData(rs);
        });

        Schedule foundSchedule = scheduleRepository.findById(1L);
        assertNotNull(foundSchedule);
        assertEquals("제목", foundSchedule.getTitle());
    }

    @Test
    void update() {

        doReturn(1).when(jdbcTemplate).update(anyString(), anyString(), anyString(), anyString(), anyString(), anyLong());

        ScheduleRequestDto requestDto = new ScheduleRequestDto();
        requestDto.setTitle("Updated Meeting");
        requestDto.setDescription("Updated Discuss Project");
        requestDto.setAssignee("bob@example.com");
        requestDto.setDate("2024-06-02");

        scheduleRepository.update(1L, requestDto);
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE schedule SET title = ?, description = ?, assignee = ?, date = ? WHERE id = ?"),
                eq("Updated Meeting"),
                eq("Updated Discuss Project"),
                eq("bob@example.com"),
                eq("2024-06-02"),
                eq(1L)
        );
    }

    @Test
    void delete() {

        doReturn(1).when(jdbcTemplate).update(anyString(), anyLong());

        scheduleRepository.delete(1L);
        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM schedule WHERE id = ?"),
                eq(1L)
        );
    }
}
