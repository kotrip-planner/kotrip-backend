package com.example.kotrip.dto.hotel;

import java.math.BigDecimal;

public interface HotelSearchInterface {
    String getAddr1();
    String getAddr2();
    String getImage_url1();
    String getImage_url2();
    BigDecimal getMap_x();
    BigDecimal getMap_y();
    String getTel();
    String getTitle();
    Long getDistance();
}
