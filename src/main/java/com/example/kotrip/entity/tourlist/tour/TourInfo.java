package com.example.kotrip.entity.tourlist.tour;

import com.example.kotrip.entity.tourlist.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "tourinfo")
public class TourInfo {
    @Id
    @Column(name = "info_id")
    public int id;

    @ManyToOne
    @JoinColumn(name = "city_id")
    public City city;

    public String addr1;

    public String addr2;

    public int imageUrl1;

    public int imageUrl2;

    @Column(name = "map_x")
    private BigDecimal mapX;

    @Column(name = "map_y")
    private BigDecimal mapY;

    private String tel;

    private String title;
}
