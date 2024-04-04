package com.example.kotrip.repository.tour;

import com.example.kotrip.dto.tour.TourInfoDto;
import com.example.kotrip.entity.tourlist.tour.TourInfo;
import com.example.kotrip.util.algorithm.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourInfoRepository extends JpaRepository<TourInfo, Long> {
    @Query("select new com.example.kotrip.dto.tour.TourInfoDto(t.title, t.imageUrl1, t.addr1, t.mapX, t.mapY) from TourInfo t where t.city.cityId = :cityId")
    List<TourInfoDto> findTourInfoByCityId(@Param("cityId") Integer cityId);

    @Query("select new com.example.kotrip.util.algorithm.Node(t.id, t.title, t.mapX, t.mapY) from TourInfo t where t.id = :infoId")
    Node findNodeByInfoId(@Param("infoId") Integer infoId);

}
