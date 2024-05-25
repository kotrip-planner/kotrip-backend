//package com.example.kotrip.repository.hotel;
//
//import com.example.kotrip.dto.hotel.HotelSearch;
//import com.example.kotrip.dto.hotel.HotelSearchResult;
//import com.example.kotrip.service.hotel.HotelSearchService;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//class HotelSearchRepositoryTest {
//    @Autowired
//    HotelRepository hotelRepository;
//    @Autowired
//    HotelSearchService hotelService;
//
//    @Test
//    public void HotelSearch() {
//        /** given */
//        Double mapAX = 129.1287274000; // 호텔런더너
//        Double mapAY = 35.1576878100;
//
//        Double mapBX = 129.1451010000; // 한화리조트 해운대
//        Double mapBY = 35.1544884700;
//
//        /** when */
//        HotelSearchResult hotelInfoDtos = hotelService.dtoTrans(mapAX, mapAY, mapBX, mapBY);
//
//        /** then */
//        for (HotelSearch hotel : hotelInfoDtos) {
//            Assertions.assertThat(hotel.distance).isLessThan(10000); // 10km
//            System.out.println("hotel.getTitle() = " + hotel.getTitle());
//        }
//    }
//}