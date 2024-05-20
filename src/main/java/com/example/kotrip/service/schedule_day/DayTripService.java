package com.example.kotrip.service.schedule_day;

import com.example.kotrip.dto.daytrip.NaverRequestDto;
import com.example.kotrip.dto.daytrip.Node;
import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.repository.schedule.ScheduleJdbcRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourJdbcRepository;
import com.example.kotrip.repository.user.UserRepository;
import com.example.kotrip.util.classification.ClassificationId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayTripService {
    private final NearNodeService nearNodeService;
    private final UserRepository userRepository;
    private final ScheduleJdbcRepository scheduleJdbcRepository;
    private final ScheduleTourJdbcRepository scheduleTourJdbcRepository;

    public ScheduleResponseDto setSchedule(NaverRequestDto naverRequestDto) {
        /**
         * NaverRequestDto는 당일치기 관광지 정보이다.
         *
         * 1. 출발지 노드에서 visited[False] 인 것 중에서 가장 가까운 노드를 찾는다. NearNodeService()
         * 2. 모든 노드를 방문할 때까지 계속한다.
         * 3. ScheduleTour(관광지)로 Schedule(당일치기)을 생성한다.
         * 4. Schedule - User 연관관계에 맞추어 파라미터에 set() 데이터를 채운다.
         * 5. 벌크 쿼리로 Schedule, ScheduleTour DB에 반영한다.
         */

        /** 1 ~ 2.
         * ScheduleTours 정의
         * - 현재 위치에서 가장 가까운 위치로 이동하는 순서로 정의한다. (그리디 알고리즘)
         */
        Map<Long, Boolean> visited = new HashMap<>();
        List<Node> nodes = naverRequestDto.getKotrip().get(0).getNodes(); // 각 관광지들

        // 관광지 리스트 미방문 초기화
        for (Node node : nodes) {
            visited.put(node.getId(), false);
        }

        ArrayList<ScheduleTour> scheduleTours = new ArrayList<>();
        Node start = nodes.get(0);
        for (int i = 0; i < nodes.size(); ++i) {
            visited.put(start.getId(), true); // 방문 처리
            scheduleTours.add(ScheduleTour.toEntity(start.getId(), start.getName(), 0L, start.getImageUrl(), start.getLongitude(), start.getLatitude(), null)); // 노드 정보로 ScheduleTour를 만든다.

            /**
             * 현재 노드에서 가까운 노드 찾기
             */
            Node nearNode = nearNodeService.getNearNode(start, nodes, visited);
            start = nearNode;
        }


        /** 3 ~ 4.
         * Schedule 객체 정의
         * - User의 히스토리에 저장할 수 있게끔, 위의 scheduleTours(여행경로)를 포함하여 Schedule 객체를 정의한다.
         * - Schedule - User 연관관계에 맞추어 User에 Schedule을 넣는다.
         *
         * Q. 이 때, 당일치기이므로 List<Schedule>로 만들어야 할 필요가 있을까?
         * A. n박 m일 DTO 구조와 동일하게 사용하기 위해 List<>로 정의하였다.
         */
        // 당일치기이므로 Schedule이 1개밖에 없으나, 기존 양식과 맞추기 위해 ArrayList로 정의했다.
        ArrayList<Schedule> schedules = new ArrayList<>();

        // 유저 조회
        Authentication authentication = getAuthentication();
        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        // 랜덤값
        String uuid = ClassificationId.getID();
        String uuid2 = ClassificationId.getID();
        Schedule schedule = Schedule.toEntity(naverRequestDto.getTitle(), uuid, naverRequestDto.getAreaId(),
                naverRequestDto.getKotrip().get(0).getDate(), user, scheduleTours, uuid2);
        schedules.add(schedule);


        /** plus.
         * ScheduleTour에 Schedule 값 넣어주기
         */
        for (int j = 0; j < schedule.getTours().size(); j++) {
            scheduleTours.get(j).setSchedule(schedule);
        }

        /** 5.
         * 벌크 쿼리로 Schedule, ScheduleTour를 DB에 반영한다.
         */
        scheduleJdbcRepository.saveAll(schedules);
        scheduleTourJdbcRepository.saveAll(scheduleTours);

        return new ScheduleResponseDto(uuid);
    }


    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
