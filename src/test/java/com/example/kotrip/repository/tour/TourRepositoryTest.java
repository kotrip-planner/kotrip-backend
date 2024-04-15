package com.example.kotrip.repository.tour;

import com.example.kotrip.dto.tour.TourInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TourRepositoryTest {
    @Autowired
    TourRepository tourRepository;

    @Test
    void execute() {
        List<TourInfoDto> tourInfoByCityId = tourRepository.findTourInfoByCityId(1);
        System.out.println(tourInfoByCityId.size());

    }
}