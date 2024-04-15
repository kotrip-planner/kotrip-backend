package com.example.kotrip.service.hotel;

import com.example.kotrip.dto.hotel.HotelSearchDto;
import com.example.kotrip.repository.hotel.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelSearchService {
    private final HotelRepository hotelRepository;

    @Transactional(readOnly = true)
    public List<HotelSearchDto> dtoTrans(BigDecimal mapX, BigDecimal mapY) {
        return hotelRepository.findHotelByCircle(mapX, mapY)
                .stream()
                .map(HotelSearchDto::new)
                .collect(Collectors.toList());
    }

}
