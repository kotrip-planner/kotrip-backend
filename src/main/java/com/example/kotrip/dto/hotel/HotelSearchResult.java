package com.example.kotrip.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HotelSearchResult {

    boolean flag;
    List<HotelSearch> hotelSearchList;
}
