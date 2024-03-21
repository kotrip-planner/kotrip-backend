package com.example.kotrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class City {
    @Id
    @Column(name = "area_id")
    public int areaId;

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
}
