package com.example.kotrip.service.daySchedule;

import com.example.kotrip.dto.schedule.response.ScheduleResponseDto;
import com.example.kotrip.dto.tour.TourInfoDto;
import com.example.kotrip.entity.schedule.Schedule;
import com.example.kotrip.entity.schedule.ScheduleTour;
import com.example.kotrip.entity.user.User;
import com.example.kotrip.naver.NaverRequestDto;
import com.example.kotrip.naver.OptimalDurationService;
import com.example.kotrip.repository.schedule.ScheduleRepository;
import com.example.kotrip.repository.scheduleTour.ScheduleTourJdbcRepository;
import com.example.kotrip.repository.tour.TourRepository;
import com.example.kotrip.repository.user.UserRepository;
import com.example.kotrip.util.classification.ClassificationId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DayScheduleService {

    private final OptimalDurationService optimalDurationService;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleTourJdbcRepository scheduleTourJdbcRepository;


    @Transactional
    public ScheduleResponseDto setSchedule(NaverRequestDto naverRequestDto) {

        Mono<List<List<Integer>>> drivingResult = optimalDurationService.getDriving(naverRequestDto);

        Authentication authentication = getAuthentication();

        User user = userRepository.findUserByNickname(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        // 관광지 순서 지정 알고리즘 + 숙소 추천 알고리즘
        // 데이터 저장

        String scheduleUuid = ClassificationId.getID();
        List<Integer> result = drivingResult.block().get(0);

        LocalDate localDate = naverRequestDto.getKotrip().get(0).getDate();
        List<Integer> tourIds = new ArrayList<>();
        List<ScheduleTour> tours = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            int tourId = result.get(i);
            tourIds.add(tourId);
        }

        List<TourInfoDto> tourInfos = tourRepository.findByIdIn(tourIds);

        for(TourInfoDto tourInfo : tourInfos) {
            tours.add(ScheduleTour.toEntity((long) tourInfo.getId(), tourInfo.getTitle(),0L,tourInfo.getImageUrl(), tourInfo.getMapY(), tourInfo.getMapX(),null));
        }

        Schedule schedule = Schedule.toEntity(scheduleUuid, naverRequestDto.getAreaId(), localDate, user, tours, ClassificationId.getID());

        for (int j = 0; j < schedule.getTours().size(); j++) {
            tours.get(j).setSchedule(schedule);
        }

        scheduleTourJdbcRepository.saveAll(tours);
        scheduleRepository.save(schedule);

        // 쿼리 한번에 날리기 - bulk insert
        return new ScheduleResponseDto(scheduleUuid);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
