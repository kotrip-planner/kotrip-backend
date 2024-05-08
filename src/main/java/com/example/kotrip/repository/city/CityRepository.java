package com.example.kotrip.repository.city;

import com.example.kotrip.dto.city.CityResponseDto;
import com.example.kotrip.entity.city.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("select new com.example.kotrip.dto.city.CityResponseDto(c) from City c")
    List<CityResponseDto> findDtoAll();

    // City id 필드명과 동일해야 함
    Optional<City> findByCityId(int cityId);
}
