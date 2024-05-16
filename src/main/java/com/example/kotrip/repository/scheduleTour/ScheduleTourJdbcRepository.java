package com.example.kotrip.repository.scheduleTour;

import com.example.kotrip.entity.schedule.ScheduleTour;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleTourJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<ScheduleTour> scheduleTours) {
        String sql = "insert into schedule_tour (duration, image_url, mapx, mapy, schedule_id, title, tour_id) values (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                scheduleTours,
                scheduleTours.size(),
                (PreparedStatement ps, ScheduleTour st) -> {
                    ps.setString(1, st.getDuration().toString());
                    ps.setString(2,st.getImageUrl());
                    ps.setString(3,st.getMapX().toString());
                    ps.setString(4,st.getMapY().toString());
                    ps.setString(5,st.getSchedule().getId().toString());
                    ps.setString(6,st.getTitle());
                    ps.setString(7,st.getTourId().toString());
                });
    }
}
