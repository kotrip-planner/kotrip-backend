package com.example.kotrip.service.schedule.multi_day_schedule;

import com.example.kotrip.dto.daytrip.request.NaverRequestDto;
import com.example.kotrip.dto.daytrip.Node;
import com.example.kotrip.dto.schedule.response.*;
import com.example.kotrip.entity.city.City;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.repository.city.CityRepository;
import com.example.kotrip.repository.schedule.ScheduleJdbcRepository;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourJdbcRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourRepository;
import com.example.kotrip.repository.user.UserRepository;
import com.example.kotrip.service.schedule.one_day_schedule.NearNodeService;
import com.example.kotrip.util.classification.ClassificationId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtherDayGreedyScheduleService {

    private static final String SET_SCHEDULE_RESULT = "일정 저장이 완료";

    private final OneDayTspScheduleService oneDayTspScheduleService;

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleJdbcRepository scheduleJdbcRepository;
    private final ScheduleTourRepository scheduleTourRepository;
    private final ScheduleTourJdbcRepository scheduleTourJdbcRepository;
    private final CityRepository cityRepository;
    private final NearNodeService nearNodeService;


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


        /**
         * bulk 쿼리로 보내주기 위함
         * !!. 추후 로직 재점검 필요
         */
        // n박 m일 이므로 Schedule이 여러 개 있다.
        ArrayList<Schedule> schedules = new ArrayList<>();
        ArrayList<ScheduleTour> scheduleTours = new ArrayList<>(); // 모든 일차 최적의 관광지 데이터 경로 저장 리스트

        Authentication authentication = getAuthentication();
        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        String scheduleUuid = ClassificationId.getID(); // 스케줄 uuid
        LocalDate localDate = naverRequestDto.getKotrip().get(0).getDate(); // 여행 시작 일자

        // 1일차 관광지 TSP 순회하여, 나온 최적의 관광지 리스트 id
        ArrayList<Long> oneDayNodePathId = oneDayTspScheduleService.getDriving(naverRequestDto);


        List<Node> nodes = naverRequestDto.getKotrip().get(0).getNodes(); // 1일차 관광지들

        Long endNodeId = oneDayNodePathId.get(oneDayNodePathId.size() - 1);
        Node endNode = nodes.stream()
                .filter(node -> node.getId().equals(endNodeId))
                .findFirst()
                .get();

        ArrayList<ScheduleTour> oneDayScheduleTour = oneDaySave(nodes, oneDayNodePathId);
        linkScheduleToEvent(oneDayScheduleTour, user, naverRequestDto, schedules, scheduleTours, scheduleUuid, localDate);

        int tripTotalDay = naverRequestDto.getKotrip().size(); // 여행이 "몇 일차"인지?
        for (int i = 1; i < tripTotalDay; ++i) {
            ArrayList<ScheduleTour> nTour = new ArrayList<>(); // n박 m일, 각 n일차 최적의 관광지 데이터 경로 저장 리스트
            List<Node> nTrip = naverRequestDto.getKotrip().get(i).getNodes(); // n일차 관광지 노드들
            Map<Long, Boolean> visited = new HashMap<>();

            // n일차 관광지 리스트 미방문 초기화
            visited.put(endNode.getId(), false);
            for (Node node : nTrip) {
                visited.put(node.getId(), false);
            }


            // n일차 마지막 노드에서, n+1일차 관광지 노드들 중 가장 가까운 노드를 start로 선정해야 함.
            for (int j = 0; j < nTrip.size() + 1; ++j) {
                visited.put(endNode.getId(), true); // 방문 처리


                // j가 0일 때는 이전 일차의 마지막 노드에서 가장 가까운 노드를 찾는다.
                // 즉, 이전 일차의 마지막 관광지 정보는 현재 일차의 스케줄에 포함하면 안 된다.
                if (j != 0) {
                    nTour.add(ScheduleTour.toEntity(endNode.getId(), endNode.getName(), 0L, endNode.getImageUrl(), endNode.getLongitude(), endNode.getLatitude(), null)); // 노드 정보로 ScheduleTour를 만든다.
                }


                if (j == nTrip.size()) { // nTrip.size() 만큼만 관광지를 저장해야 한다.
                    break;
                }
                /**
                 * 현재 노드에서 가까운 노드 찾기
                 */
                Node nearNode = nearNodeService.getNearNode(endNode, nTrip, visited);
                endNode = nearNode;
            }


            // 여행 일차가 늘어나는 만큼, localDate도 계속 늘려주어야 한다.
            if(i >= 1) {
                localDate = localDate.plusDays(1);
            }


            linkScheduleToEvent(nTour, user, naverRequestDto, schedules, scheduleTours, scheduleUuid, localDate);

        }


        scheduleJdbcRepository.saveAll(schedules);
        scheduleTourJdbcRepository.saveAll(scheduleTours);

        return new ScheduleResponseDto(scheduleUuid);
    }

    private ArrayList<ScheduleTour> oneDaySave(List<Node> nodes, ArrayList<Long> oneDayNodePathId) {
        List<Node> oneDayNodes = nodes.stream()
                .filter(node -> oneDayNodePathId.contains(node.getId()))
                .collect(Collectors.toList());

        ArrayList<ScheduleTour> nTour = new ArrayList<>();
        for (int i = 0; i < oneDayNodes.size(); ++i) {
            Node endNode = oneDayNodes.get(i);
            nTour.add(ScheduleTour.toEntity(endNode.getId(), endNode.getName(), 0L, endNode.getImageUrl(), endNode.getLongitude(), endNode.getLatitude(), null)); // 노드 정보로 ScheduleTour를 만든다.
        }

        return nTour;
    }

    public void linkScheduleToEvent(ArrayList<ScheduleTour> nTour, User user, NaverRequestDto naverRequestDto, ArrayList<Schedule> schedules, ArrayList<ScheduleTour> scheduleTours, String scheduleUuid, LocalDate localDate) {

        // 랜덤값
        String uuid2 = ClassificationId.getID();
        Schedule schedule = Schedule.toEntity(naverRequestDto.getTitle(), scheduleUuid, naverRequestDto.getAreaId(),
                localDate, user, nTour, uuid2);
        schedules.add(schedule);

        /** plus.
         * ScheduleTour에 Schedule 값 넣어주기
         */
        for (int j = 0; j < schedule.getTours().size(); j++) {
            ScheduleTour scheduleTour = nTour.get(j).setSchedule(schedule);
            scheduleTours.add(scheduleTour);
        }
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
            City city = cityRepository.findById((long) map.get(key).get(0).getAreaId())
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
