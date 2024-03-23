package com.example.kotrip.entity.tourlist.tour;

import com.example.kotrip.entity.tourlist.City;
import com.example.kotrip.entity.tourlist.hotel.Hotel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @OneToOne(mappedBy = "tourInfo")
    public Tour tour;

    public String addr1;

    public String addr2;

    public String imageUrl1;

    public String imageUrl2;

    @Column(name = "map_x", precision = 13, scale = 10)
    private BigDecimal mapX;

    @Column(name = "map_y", precision = 13, scale = 10)
    private BigDecimal mapY;

    private String tel;

    @NotNull
    private String title;
}
