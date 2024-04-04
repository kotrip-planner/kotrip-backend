package com.example.kotrip.service.schedule;

import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.naver.OptimalDurationService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String SET_SCHEDULE_RESULT = "일정 저장이 완료";

    private final OptimalDurationService optimalDurationService;

    // 일차별 스케줄을 생성하는 api
    public ScheduleResponseDto setSchedule(Authentication authentication, NaverRequestDto naverRequestDto){

        //User user = customUserDetailsService.loadUserByUsername(authentication.getName());

        // 관광지 순서 지정 알고리즘 + 숙소 추천 알고리즘
        // 데이터 저장

        Mono<Map<String, List<List<Integer>>>> optimalResult = null;

        Mono<List<List<Integer>>> drivingResult = optimalDurationService.getDriving(naverRequestDto);
        optimalResult = drivingResult.flatMap(driving -> optimalDurationService.getOptimalRoute(naverRequestDto, driving)
                    .map(optimalRoute -> Collections.singletonMap("optimalRoute", optimalRoute)));

        optimalResult.subscribe(
                data -> {
                    System.out.println(data);
                    List<List<Integer>> result = data.get("optimalRoute");

                    // DB에 저장 - User에 맞게
                },
                error -> {
                    System.out.println(error);
                },
                () -> {
                    System.out.println("Completed");
                }
        );

        // 최적 경로 만든 후 저장
        return new ScheduleResponseDto(SET_SCHEDULE_RESULT);
    }
}