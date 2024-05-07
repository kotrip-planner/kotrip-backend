package com.example.kotrip.entity.tour;

import com.example.kotrip.entity.city.City;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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

    public String imageUrl1;

    public String imageUrl2;

    @Column(name = "map_x")
    private Double mapX;

    @Column(name = "map_y")
    private Double mapY;

    private String tel;

    @NotNull
    private String title;
}
