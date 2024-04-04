package com.example.kotrip.entity.tourlist.hotel;

import com.example.kotrip.entity.tourlist.City;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

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
