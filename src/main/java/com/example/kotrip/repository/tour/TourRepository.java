package com.example.kotrip.repository.tour;

import com.example.kotrip.dto.tour.TourInfoDto;
import com.example.kotrip.entity.tourlist.tour.TourInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TourRepository extends JpaRepository<TourInfo, Long> {
    @Query("select new com.example.kotrip.dto.tour.TourInfoDto(t.id, t.title, t.imageUrl1, t.addr1, t.mapX, t.mapY) from TourInfo t where t.city.cityId = :cityId")
    List<TourInfoDto> findTourInfoByCityId(@Param("cityId") Integer cityId);

    Optional<TourInfo> findTourInfoById(Integer id);
}
