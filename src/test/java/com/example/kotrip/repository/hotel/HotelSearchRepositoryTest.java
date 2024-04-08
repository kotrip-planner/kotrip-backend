package com.example.kotrip.repository.hotel;

import com.example.kotrip.dto.hotel.HotelSearchDto;
import com.example.kotrip.service.hotel.HotelSearchService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Transactional
class HotelSearchRepositoryTest {
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    HotelSearchService hotelService;

    @Test
    public void HotelSearch() {
        BigDecimal mapX = new BigDecimal(129.1287274000);
        BigDecimal mapY = new BigDecimal(35.1576878100);

        List<HotelSearchDto> hotelInfoDtos = hotelService.dtoTrans(mapX, mapY);
        for (HotelSearchDto hotel : hotelInfoDtos) {
            Assertions.assertThat(hotel.distance).isLessThan(5000);
        }
    }
}