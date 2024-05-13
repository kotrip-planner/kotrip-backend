package com.example.kotrip.service.schedule;

import com.example.kotrip.dto.schedule.response.ScheduleEachResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleTourResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleToursResponseDto;
import com.example.kotrip.dto.schedule.response.SchedulesResponseDto;
import com.example.kotrip.dto.tour.TourInfoDto;
import com.example.kotrip.entity.city.City;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.naver.OptimalDurationService;
import com.example.kotrip.repository.city.CityRepository;
import com.example.kotrip.repository.schedule.ScheduleJdbcRepository;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourJdbcRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourRepository;
import com.example.kotrip.repository.tour.TourRepository;
import com.example.kotrip.repository.user.UserRepository;
import com.example.kotrip.util.classification.ClassificationId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final CityRepository cityRepository;
    private final TourRepository tourRepository;

    // 일차별 스케줄을 생성하는 api
    @Transactional
    public ScheduleResponseDto setSchedule(NaverRequestDto naverRequestDto) {

        /**
         * 1. 비동기 api 호출을 위해, kotlin 으로 최적 경로 리스트를 추출한다.
         * 2. 결과값에서 각 관광지 id에 따른 TourInfo 데이터를 이용해, ScheduleTour(n일차의 모든 관광지)를 생성한다. (List<ScheduleTour>)
         * 3. 그렇게 각 일차의 ScheduleTour 정보로 Schedule(n일차)를 생성한다.
         * 4. ScheduleTour - Schedule / Schedule - User 연관관계에 맞추어 파라미터에 set() 데이터를 채운다.
         * 5. 벌크 쿼리로 DB에 반영한다.
         */

        // 관광지 TSP 순회 리스트
        // example : [[1,2,3,4,1], [5,6,7,8,5] ...]
        List<List<Integer>> driving = optimalDurationService.getDriving(naverRequestDto).block();

        // 최소경로를 이용한 최적 경로 리스트
        // example : [[1,2,3],[4,5,6],[7,8,9]...]
        Mono<List<List<Integer>>> drivingResult = optimalDurationService.getOptimalRoute(naverRequestDto, driving);

        Authentication authentication = getAuthentication();

        // 유저 조회
        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));


        // 관광지 순서 지정 알고리즘 + 숙소 추천 알고리즘
        // 데이터 저장
        String scheduleUuid = ClassificationId.getID();
        String title = naverRequestDto.getTitle();
        List<List<Integer>> result = drivingResult.block();

        LocalDate localDate = naverRequestDto.getKotrip().get(0).getDate();
        List<Schedule> schedules = new ArrayList<>();
        List<ScheduleTour> scheduleTours = new ArrayList<>();
        List<Integer> tourIds = new ArrayList<>();

        // DB에 저장 - User에 맞게
        for (int i = 0; i < result.size(); i++) {
            List<ScheduleTour> tours = new ArrayList<>(); // n일차 관광지 리스트
            for (int j = 0; j < result.get(i).size(); j++) {
                int tourId = result.get(i).get(j); // 각 관광지 id
                tourIds.add(tourId);
            }

            List<TourInfoDto> tourInfoDtos = new ArrayList<>(); // n일차 관광지 투어 정보
            for (Integer tourId : tourIds) {
                TourInfoDto tourInfoDto = tourRepository.findByTourid(tourId); // 관광지 id로 TourInfoDto 가져오기
                tourInfoDtos.add(tourInfoDto);
            }
          
            tourIds.clear();

            // List<ScheduleTour> tours
            // 사용자가 선택한 TourInfoDto로, DB에 들어갈 ScheduleTour을 생성한다. (스케줄을 그대로 가져오기 위함)
            for(TourInfoDto tourInfo : tourInfoDtos) {
                tours.add(ScheduleTour.toEntity((long) tourInfo.getId(), tourInfo.getTitle(),0L, tourInfo.getImageUrl(), tourInfo.getMapY(), tourInfo.getMapX(),null));
            }

            // schedule과 id를 묶어주어야 함
            if(i >= 1) {
                localDate = localDate.plusDays(1);
            }

            // 사용자, 생성한 ScheduleTour List와 localDate 등으로 Schedule을 생성한다.
            Schedule schedule = Schedule.toEntity(title, scheduleUuid, naverRequestDto.getAreaId(), localDate, user, tours, ClassificationId.getID());
            schedules.add(schedule); //

            // ScheduleTour에 Schedule을 부여해 묶어준다.
            for (int j = 0; j < schedule.getTours().size(); j++) {
                ScheduleTour scheduleTour = tours.get(j).setSchedule(schedule);
                scheduleTours.add(scheduleTour);
            }

        }


        System.out.println("schedule : " + schedules.get(0).toString());
        System.out.println("scheduleTours : " + scheduleTours.get(0).toString());

        // 쿼리 한번에 날리기 - bulk insert
        scheduleJdbcRepository.saveAll(schedules);
        scheduleTourJdbcRepository.saveAll(scheduleTours);

        // 최적 경로 만든 후 저장
        return new ScheduleResponseDto(scheduleUuid);
    }

    public SchedulesResponseDto getSchedule() { // 스케줄 가져오는 함수
        Authentication authentication = getAuthentication();

        log.info("스케줄 가져오는 함수 실행");

        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Not found User"));
        List<Schedule> schedules = scheduleRepository.findSchedulesByUserOrderByTime(user).orElseThrow(() -> new UsernameNotFoundException("Not found schedule"));

        List<ScheduleToursResponseDto> scheduleToursResponseDtos = new ArrayList<>();
        List<ScheduleEachResponseDto> scheduleEachResponseDtos = new ArrayList<>();

        HashMap<String, List<ScheduleToursResponseDto>> map = new HashMap<>();

        String first = schedules.get(0).getClassificationId();
        for(Schedule schedule: schedules) { // 스케줄 전부 가져요기

            String classificationId = schedule.getClassificationId();
            String title = schedule.getTitle();
            Integer areaId = schedule.getAreaId();

            if(!classificationId.equals(first)) {
                scheduleToursResponseDtos = new ArrayList<>(); // 초기화
                first = classificationId;
            }

            List<ScheduleTourResponseDto> tours = scheduleTourRepository.findScheduleToursBySchedule(schedule).orElseThrow(() -> new UsernameNotFoundException("Not found Schedule"))
                    .stream().map(tour -> ScheduleTourResponseDto.builder().id(tour.getId()).title(tour.getTitle()).imageUrl(tour.getImageUrl()).mapX(tour.getMapX()).mapY(tour.getMapY()).build()).collect(
                            Collectors.toList());

            scheduleToursResponseDtos.add(ScheduleToursResponseDto.builder().title(title).areaId(areaId).tours(tours).date(schedule.getTime()).build());

            map.put(classificationId, scheduleToursResponseDtos);
        }

        for(String key : map.keySet()) {
            City city = cityRepository.findById(map.get(key).get(0).getAreaId())
                    .orElseThrow(() -> new IllegalArgumentException("도시가 존재하지 않는다."));
            scheduleEachResponseDtos.add(ScheduleEachResponseDto.builder().title(map.get(key).get(0).getTitle()).city(city.title).uuid(key).schedule(map.get(key)).build());
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
