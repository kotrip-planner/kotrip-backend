package com.example.kotrip.repository.scheduleTour;

import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTourRepository extends JpaRepository<ScheduleTour, Long> {
    Optional<List<ScheduleTour>> findScheduleToursBySchedule(Schedule schedule);
}
