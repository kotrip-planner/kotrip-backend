package com.example.kotrip.entity.city;

import com.example.kotrip.entity.hotel.HotelInfo;
import com.example.kotrip.entity.tour.TourInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
    public double mapX;

    @NotEmpty
    @Column(name = "mapy")
    public double mapY;

    @OneToMany(mappedBy = "city")
    public List<TourInfo> tours;

    @OneToMany(mappedBy = "city")
    public List<HotelInfo> hotels;
}
