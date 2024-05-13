package com.example.kotrip.service.hotel;

import com.example.kotrip.dto.hotel.HotelSearchDto;
import com.example.kotrip.repository.hotel.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelSearchService {
    private final HotelRepository hotelRepository;

    private static final int EARTH_RADIUS_KM = 6371;

    /**
     * 두 지점(위도, 경도) 사이의 거리를 계산합니다.
     *
     * @param lat1 첫 번째 지점의 위도
     * @param lon1 첫 번째 지점의 경도
     * @param lat2 두 번째 지점의 위도
     * @param lon2 두 번째 지점의 경도
     * @return 두 지점 사이의 거리(KM 단위)
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c / 2;
    }

    @Transactional(readOnly = true)
    public List<HotelSearchDto> dtoTrans(Double mapAX, Double mapAY, Double mapBX, Double mapBY) {

        // 중점 좌
        Double mapX = (mapAX + mapBX) / 2;
        Double mapY = (mapAY + mapBY) / 2;
        Double radius = calculateDistance(mapX, mapY, mapAX, mapAY);

        log.info("radius={}", radius);

        return hotelRepository.findHotelByCircle(mapX, mapY, radius * 1000)
                .stream()
                .map(HotelSearchDto::new)
                .collect(Collectors.toList());
    }
}
