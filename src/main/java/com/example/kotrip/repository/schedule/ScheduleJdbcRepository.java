package com.example.kotrip.repository.schedule;

import com.example.kotrip.entity.schedule.Schedule;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<Schedule> schedules) {
        String sql = "INSERT INTO schedule (area_id, classification_id, time, user_id, schedule_id, title) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                schedules,
                schedules.size(),
                (PreparedStatement ps, Schedule s) -> {
                    ps.setString(1, s.getAreaId().toString());
                    ps.setString(2, s.getClassificationId().toString());
                    ps.setString(3, s.getTime().toString());
                    ps.setString(4, s.getUser().getUserId().toString());
                    ps.setString(5, s.getId());
                    ps.setString(6, s.getTitle());
                });
    }
}
