package com.example.kotrip.service.history;

import com.example.kotrip.dto.history.response.HistoryEachResponseDto;
import com.example.kotrip.dto.history.response.HistoryResponseDto;
import com.example.kotrip.dto.schedule.request.ScheduleUuidDto;
import com.example.kotrip.dto.schedule.response.ScheduleEachResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleTourResponseDto;
import com.example.kotrip.dto.schedule.response.ScheduleToursResponseDto;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import com.example.kotrip.entity.city.City;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.repository.city.CityRepository;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourRepository;
import com.example.kotrip.repository.user.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleTourRepository scheduleTourRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public HistoryResponseDto getHistory(){

        Authentication authentication = getAuthentication();

        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findSchedulesByUserOrderByTime(user).orElseThrow(() -> new UsernameNotFoundException("Not found schedule"));
        List<HistoryEachResponseDto> historyEachResponseDtos = new ArrayList<>();

        HashMap<String, List<Schedule>> map = new HashMap<>();
        for (Schedule schedule : schedules) {
            String id = schedule.getClassificationId();
            if(map.get(id) == null){
                List<Schedule> list = new ArrayList<>();
                list.add(schedule);
                map.put(id, list);
            }else {
                map.get(id).add(schedule);
            }
        }

        for(String key: map.keySet()) {
            List<Schedule> list = map.get(key);
            LocalDate startDate = list.get(0).getTime();
            LocalDate endDate = list.get(list.size()-1).getTime();
            int areaId = list.get(0).getAreaId();

            City city = cityRepository.findById(areaId).orElseThrow(() -> new IllegalArgumentException("지역에 맞는 도시를 찾을 수 없습니다."));

            historyEachResponseDtos.add(
                    HistoryEachResponseDto
                            .builder()
                            .startDate(startDate)
                            .endDate(endDate)
                            .uuid(key)
                            .title(city.title)
                            .imageUrl(city.imageUrl)
                            .build()
            );

        }

        return HistoryResponseDto.builder().history(historyEachResponseDtos).build();
    }

    public ScheduleEachResponseDto getSchedule(ScheduleUuidDto scheduleUuidDto){
        String uuid = scheduleUuidDto.getUuid();

        // uuid에 맞는 스케줄 가져오기
        List<Schedule> schedules = scheduleRepository.findSchedulesByClassificationIdOrderByTime(uuid).orElseThrow(() -> new IllegalArgumentException("스케줄이 존재하지 않습니다."));

        String title = schedules.get(0).getTitle();

        int areaId = schedules.get(0).getAreaId();
        City city = cityRepository.findById(areaId).orElseThrow(() -> new IllegalArgumentException("도시가 존재하지 않습니다."));

        ScheduleToursResponseDto scheduleToursResponseDto = null;
        List<ScheduleToursResponseDto> scheduleToursResponseDtos = new ArrayList<>();

        for(Schedule schedule : schedules) {
            List<ScheduleTour> scheduleTours = scheduleTourRepository.findScheduleToursBySchedule(schedule).orElseThrow(() -> new IllegalArgumentException("스케줄에 걸맞는 여행이 존재하지 않습니다."));
            List<ScheduleTourResponseDto> list = new ArrayList<>();
            for(ScheduleTour scheduleTour : scheduleTours) {
                ScheduleTourResponseDto responseDto = ScheduleTourResponseDto.builder()
                        .title(scheduleTour.getTitle())
                        .mapY(scheduleTour.getMapY())
                        .mapX(scheduleTour.getMapX())
                        .imageUrl(scheduleTour.getImageUrl())
                        .id(scheduleTour.getId())
                        .build();

                list.add(responseDto);
            }

            scheduleToursResponseDto = ScheduleToursResponseDto.builder()
                    .tours(list)
                    .date(schedule.getTime())
                    .build();

            scheduleToursResponseDtos.add(scheduleToursResponseDto);
        }

        log.info("{}", scheduleToursResponseDto);

        return ScheduleEachResponseDto.builder()
                .title(title)
                .schedule(scheduleToursResponseDtos)
                .city(city.getTitle())
                .uuid(uuid)
                .build();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
