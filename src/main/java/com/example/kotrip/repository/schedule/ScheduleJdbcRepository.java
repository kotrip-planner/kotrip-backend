package com.example.kotrip.repository.schedule;

import com.example.kotrip.entity.schedule.Schedule;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ScheduleJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<Schedule> schedules) {

        String sql = "INSERT INTO schedule (area_id, classification_id, time, user_id, schedule_id, title) VALUES (?, ?, ?, ?, ?, ?)";

        log.info("schedules.size() = {}", schedules.size());
        jdbcTemplate.batchUpdate(sql, // bulk insert에 사용할 기본 쿼리
                schedules,  // insert할 모델
                schedules.size(), // 1번의 batch로 함께 insert할 batch 사이즈
                (PreparedStatement ps, Schedule s) -> { // 쿼리의 ?의 순서대로 1번으로 할당되며 해당 쿼리 ? 대신 치환
                    ps.setString(1, s.getAreaId().toString());
                    ps.setString(2, s.getClassificationId().toString());
                    ps.setString(3, s.getTime().toString());
                    ps.setString(4, s.getUser().getUserId().toString());
                    ps.setString(5, s.getId());
                    ps.setString(6, s.getTitle());
                });
    }
}
