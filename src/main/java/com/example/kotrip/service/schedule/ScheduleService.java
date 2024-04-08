package com.example.kotrip.service.schedule;

import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import com.example.kotrip.entity.tourlist.tour.TourInfo;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.naver.OptimalDurationService;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.tour.TourRepository;
import com.example.kotrip.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String SET_SCHEDULE_RESULT = "일정 저장이 완료";

    private final OptimalDurationService optimalDurationService;

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final TourRepository tourRepository;

    // 일차별 스케줄을 생성하는 api
    public ScheduleResponseDto setSchedule(NaverRequestDto naverRequestDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        // 관광지 순서 지정 알고리즘 + 숙소 추천 알고리즘
        // 데이터 저장

        Mono<Map<String, List<List<Integer>>>> optimalResult = null;

        Mono<List<List<Integer>>> drivingResult = optimalDurationService.getDriving(naverRequestDto);
        optimalResult = drivingResult.flatMap(driving -> optimalDurationService.getOptimalRoute(naverRequestDto, driving)
                    .map(optimalRoute -> Collections.singletonMap("optimalRoute", optimalRoute)));

        optimalResult.subscribe(
                data -> {
                    List<List<Integer>> result = data.get("optimalRoute");

                    // DB에 저장 - User에 맞게
                    for (int i = 0; i < result.size(); i++) {
                        List<ScheduleTour> tours = new ArrayList<>();
                        for (int j = 0; j < result.get(i).size(); j++) {
                            int tourId = result.get(i).get(j);
                            TourInfo tourInfo = tourRepository.findTourInfoById(tourId).orElseThrow(() -> new IllegalArgumentException("여행 정보 가져오기"));
                            String imageUrl = getImageUrl(tourInfo);
                            tours.add(ScheduleTour.toEntity((long) tourInfo.getId(), tourInfo.getTitle(),0L,imageUrl, tourInfo.getMapY(), tourInfo.getMapX()));
                        }
                        // schedule
                    }
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

    private String getImageUrl(TourInfo tourInfo) {

        if(tourInfo.getImageUrl1() != null) {
            return tourInfo.getImageUrl1();
        }

        return tourInfo.getImageUrl2();
    }
}