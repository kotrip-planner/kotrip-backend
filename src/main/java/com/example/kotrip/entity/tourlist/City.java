package com.example.kotrip.entity.tourlist;

import com.example.kotrip.entity.tourlist.hotel.HotelInfo;
import com.example.kotrip.entity.tourlist.tour.TourInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class City {
    @Id
    @Column(name = "city_id")
    public int cityId;

    @NotEmpty
    public String title;

    @Column(name = "image_url")
    public String imageUrl;

    @NotEmpty
    @Column(name = "mapx")
    public long mapX;

    @NotEmpty
    @Column(name = "mapy")
    public long mapY;

    @OneToMany(mappedBy = "city")
    public List<TourInfo> tours;

    @OneToMany(mappedBy = "city")
    public List<HotelInfo> hotels;
}
