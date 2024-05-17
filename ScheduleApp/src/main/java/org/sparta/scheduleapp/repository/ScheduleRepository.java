package org.sparta.scheduleapp.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparta.scheduleapp.controller.ScheduleController;
import org.sparta.scheduleapp.dto.ScheduleRequestDto;
import org.sparta.scheduleapp.dto.ScheduleResponseDto;
import org.sparta.scheduleapp.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@AllArgsConstructor
@Component
public class ScheduleRepository {
    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final JdbcTemplate jdbcTemplate;

    public Schedule save(Schedule schedule) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO schedule (title, description, assignee, date, password, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, schedule.getTitle());
            preparedStatement.setString(2, schedule.getDescription());
            preparedStatement.setString(3, schedule.getAssignee());
            preparedStatement.setString(4, schedule.getDate());
            preparedStatement.setString(5, schedule.getPassword());
            preparedStatement.setBoolean(6, schedule.isDeleted());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        schedule.setId(id);
        return schedule;
    }

    public List<ScheduleResponseDto> findAll() {
        log.info("Fetching schedule list");
        String sql = "SELECT * FROM schedule";
        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String assignee = rs.getString("assignee");
                String date = rs.getString("date");
                String password = rs.getString("password");
                boolean deleted = rs.getBoolean("is_deleted");

                return null;
            }
        });
    }

    public Schedule findById(Long id) {
        String sql = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getLong("id"));
                schedule.setTitle(resultSet.getString("title"));
                schedule.setDescription(resultSet.getString("description"));
                schedule.setAssignee(resultSet.getString("assignee"));
                schedule.setDate(resultSet.getString("date"));
                schedule.setPassword(resultSet.getString("password"));
                schedule.setDeleted(resultSet.getBoolean("is_deleted"));
                return schedule;
            } else {
                return null;
            }
        }, id); // jdbcTemplate.queryForObject(sql:(), id);
    }

    public void update(Long id, ScheduleRequestDto requestDto) {
        String sql = "UPDATE schedule SET title = ?, description = ?, assignee = ?, date = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getDescription(), requestDto.getAssignee(), requestDto.getDate(), id);

    }

    public void delete(Long id) {
        // 삭제된 값인지 확인후 삭제
        String sql = "UPDATE schedule SET is_deleted = true WHERE id = ?";
        sql = "DELETE FROM schedule WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    // ResultSet을 Schedule 객체로 변환하는 RowMapper 클래스
    public static class ScheduleRowMapper implements RowMapper<Schedule> {
        @Override
        public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
            Schedule schedule = new Schedule();
            schedule.setId(rs.getLong("id"));
            schedule.setTitle(rs.getString("title"));
            schedule.setDescription(rs.getString("description"));
            schedule.setAssignee(rs.getString("assignee"));
            schedule.setDate(rs.getString("date"));
            schedule.setPassword(rs.getString("password"));
            schedule.setDeleted(rs.getBoolean("is_deleted"));
            return schedule;
        }
    }
}
