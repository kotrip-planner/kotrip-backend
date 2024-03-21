package com.example.kotrip.controller;

import com.example.kotrip.dto.city.CityResponseDto;
import com.example.kotrip.entity.City;
import com.example.kotrip.repository.city.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CityController {
    private final CityRepository cityRepository;

    @GetMapping("/city")
    public List<CityResponseDto> cityList() {
        List<City> cities = cityRepository.findAll();
        List<CityResponseDto> result = cities.stream()
                .map(o -> new CityResponseDto(o))
                .collect(Collectors.toList());
        return result;
    }
}
