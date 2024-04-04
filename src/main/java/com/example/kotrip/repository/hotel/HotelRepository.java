package com.example.kotrip.repository.hotel;

import com.example.kotrip.dto.hotel.HotelSearchInterface;
import com.example.kotrip.entity.tourlist.hotel.HotelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface HotelRepository extends JpaRepository<HotelInfo, Long> {
    @Query(value = "select addr1, addr2, image_url1, image_url2, map_x, map_y, tel, title," +
            " ST_Distance_Sphere(" +
            " point(map_x, map_y)," +
            " point(:mapX, :mapY)) AS distance" +
            " FROM hotelinfo" +
            " HAVING distance <= 5000" +
            " ORDER BY distance ASC;", nativeQuery = true)
    List<HotelSearchInterface> findHotelByCircle(@Param("mapX") BigDecimal mapX, @Param("mapY") BigDecimal mapY);
}
