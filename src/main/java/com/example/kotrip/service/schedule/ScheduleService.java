package com.example.kotrip.service.schedule;

import com.example.kotrip.dto.schedule.request.ScheduleRequestDto;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.service.useDetails.CustomUserDetailsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public ScheduleResponseDto getSchedule(Authentication authentication, ScheduleRequestDto scheduleRequestDto){

        User user = customUserDetailsService.loadUserByUsername(authentication.getName());

        List<Schedule> schedules = scheduleRepository.findSchedulesByUser(user).orElseThrow(() -> new RuntimeException());

        // 관광지 순서 지정 알고리즘 + 숙소 추천 알고리즘
        // 데이터 저장

        return ScheduleResponseDto.of(schedules);
    }
}
