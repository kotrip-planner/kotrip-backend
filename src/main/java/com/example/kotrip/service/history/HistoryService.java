package com.example.kotrip.service.history;

import com.example.kotrip.dto.history.response.HistoryEachResponseDto;
import com.example.kotrip.dto.history.response.HistoryResponseDto;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.tourlist.City;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.repository.city.CityRepository;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.user.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public HistoryResponseDto getHistory(){

        Authentication authentication = getAuthentication();

        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        List<Schedule> schedules = scheduleRepository.findSchedulesByUser(user).orElseThrow(() -> new UsernameNotFoundException("Not found schedule"));
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

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
