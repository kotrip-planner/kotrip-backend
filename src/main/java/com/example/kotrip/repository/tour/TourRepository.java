package com.example.kotrip.repository.tour;

import com.example.kotrip.dto.tour.response.TourInfoDto;
import com.example.kotrip.entity.tour.TourInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourRepository extends JpaRepository<TourInfo, Long> {
    @Query("select new com.example.kotrip.dto.tour.response.TourInfoDto(t.id,t.title, t.imageUrl1, t.addr1, t.mapX, t.mapY) from TourInfo t where t.city.cityId = :cityId")
    List<TourInfoDto> findTourInfoByCityId(@Param("cityId") Integer cityIds, Pageable pageable);

    @Query("select new com.example.kotrip.dto.tour.response.TourInfoDto(t.id,t.title, t.imageUrl1, t.addr1, t.mapX, t.mapY) from TourInfo t")
    List<TourInfoDto> ex();

    @Query("SELECT distinct new com.example.kotrip.dto.tour.response.TourInfoDto(ti.id, ti.title, ti.imageUrl1, ti.imageUrl2, ti.mapY, ti.mapX) FROM TourInfo ti WHERE ti.id IN :ids")
    List<TourInfoDto> findByIdIn(List<Integer> ids);

    @Query("SELECT distinct new com.example.kotrip.dto.tour.response.TourInfoDto(ti.id, ti.title, ti.imageUrl1, ti.imageUrl2, ti.mapY, ti.mapX) FROM TourInfo ti WHERE ti.id =:id")
    TourInfoDto findByTourid(Integer id);
}
