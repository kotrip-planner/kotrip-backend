package com.example.kotrip.repository.schedule;

import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    Optional<List<Schedule>> findSchedulesByUser(User user);
}
