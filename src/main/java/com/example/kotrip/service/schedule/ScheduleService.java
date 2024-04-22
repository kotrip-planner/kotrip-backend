package com.example.kotrip.service.schedule;

import com.example.kotrip.dto.schedule.response.ScheduleEachResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleTourResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleToursResponseDto;
import com.example.kotrip.dto.schedule.response.SchedulesResponseDto;
import com.example.kotrip.dto.tour.TourInfoDto;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.naver.OptimalDurationService;
import com.example.kotrip.repository.schedule.ScheduleJdbcRepository;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourJdbcRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourRepository;
import com.example.kotrip.repository.tour.TourRepository;
import com.example.kotrip.repository.user.UserRepository;
import com.example.kotrip.util.classification.ClassificationId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String SET_SCHEDULE_RESULT = "일정 저장이 완료";

    private final OptimalDurationService optimalDurationService;

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleJdbcRepository scheduleJdbcRepository;
    private final ScheduleTourRepository scheduleTourRepository;
    private final ScheduleTourJdbcRepository scheduleTourJdbcRepository;
    private final TourRepository tourRepository;

    // 일차별 스케줄을 생성하는 api
    @Transactional
    public ScheduleResponseDto setSchedule(NaverRequestDto naverRequestDto){

        Authentication authentication = getAuthentication();

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
                    LocalDate localDate = naverRequestDto.getKotrip().get(0).getDate();
                    List<Schedule> schedules = new ArrayList<>();
                    List<ScheduleTour> scheduleTours = new ArrayList<>();
                    List<Integer> tourIds = new ArrayList<>();

                    String scheduleUuid = ClassificationId.getID();
                    // DB에 저장 - User에 맞게
                    for (int i = 0; i < result.size(); i++) {
                        List<ScheduleTour> tours = new ArrayList<>();
                        for (int j = 0; j < result.get(i).size(); j++) {
                            int tourId = result.get(i).get(j);
                            tourIds.add(tourId);
                        }

                        List<TourInfoDto> tourInfos = tourRepository.findByIdIn(tourIds);
                        tourIds.clear();
                        for(TourInfoDto tourInfo : tourInfos) {
                            tours.add(ScheduleTour.toEntity((long) tourInfo.getId(), tourInfo.getTitle(),0L,tourInfo.getImageUrl(), tourInfo.getMapY(), tourInfo.getMapX(),null));
                        }

                        // schedule과 id를 묶어주어야 함
                        if(i >= 1) {
                            localDate = localDate.plusDays(1);
                        }

                        Schedule schedule = Schedule.toEntity(scheduleUuid, naverRequestDto.getAreaId(), localDate, user, tours);
                        schedules.add(schedule);

                        for (int j = 0; j < schedule.getTours().size(); j++) {
                            ScheduleTour scheduleTour = tours.get(j).setSchedule(schedule);
                            scheduleTours.add(scheduleTour);
                        }

                    }

                    // 쿼리 한번에 날리기
                    scheduleRepository.saveAll(schedules);
                    scheduleTourJdbcRepository.saveAll(scheduleTours);
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

    public SchedulesResponseDto getSchedule() { // 스케줄 가져오는 함수
        Authentication authentication = getAuthentication();

        log.info("스케줄 가져오는 함수 실행");

        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Not found User"));
        List<Schedule> schedules = scheduleRepository.findSchedulesByUser(user).orElseThrow(() -> new UsernameNotFoundException("Not found schedule"));

        List<ScheduleToursResponseDto> scheduleToursResponseDtos = new ArrayList<>();
        List<ScheduleEachResponseDto> scheduleEachResponseDtos = new ArrayList<>();

        HashMap<String, List<ScheduleToursResponseDto>> map = new HashMap<>();

        String first = schedules.get(0).getClassificationId();
        for(Schedule schedule: schedules) { // 스케줄 전부 가져요기

            String classificationId = schedule.getClassificationId();

            if(!classificationId.equals(first)) {
                scheduleToursResponseDtos = new ArrayList<>(); // 초기화
                first = classificationId;
            }

            log.info("id : {}",classificationId);

            List<ScheduleTourResponseDto> tours = scheduleTourRepository.findScheduleToursBySchedule(schedule).orElseThrow(() -> new UsernameNotFoundException("Not found Schedule"))
                    .stream().map(tour -> ScheduleTourResponseDto.builder().id(tour.getId()).title(tour.getTitle()).imageUrl(tour.getImageUrl()).mapX(tour.getMapX()).mapY(tour.getMapY()).build()).collect(
                            Collectors.toList());

            scheduleToursResponseDtos.add(ScheduleToursResponseDto.builder().tours(tours).date(schedule.getTime()).build());

            map.put(classificationId, scheduleToursResponseDtos);
        }

        for(String key : map.keySet()) {
            scheduleEachResponseDtos.add(ScheduleEachResponseDto.builder().uuid(key).schedule(map.get(key)).build());
        }

        log.info("schedulesTourResponseDtos : {}", scheduleToursResponseDtos);
        return SchedulesResponseDto.builder()
                .schedules(scheduleEachResponseDtos)
                .build();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}