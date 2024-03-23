package com.example.kotrip.entity.tourlist.hotel;

import com.example.kotrip.entity.tourlist.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "hotelinfo")
public class HotelInfo {
    @Id
    @Column(name = "info_id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    public City city;

    @OneToOne(mappedBy = "hotelInfo")
    public Hotel hotel;

    public String addr1;

    public String addr2;

    public String imageUrl1;

    public String imageUrl2;

    @Column(name = "map_x", precision = 13, scale = 10)
    public BigDecimal mapX;

    @Column(name = "map_y", precision = 13, scale = 10)
    public BigDecimal mapY;

    public String tel;

    public String title;
}
