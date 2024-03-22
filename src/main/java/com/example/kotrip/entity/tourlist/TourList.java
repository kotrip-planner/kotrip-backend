package com.example.kotrip.entity.tourlist;

import com.example.kotrip.entity.tourlist.hotel.Hotel;
import com.example.kotrip.entity.tourlist.hotel.HotelInfo;
import com.example.kotrip.entity.tourlist.tour.Tour;
import com.example.kotrip.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TourList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tourlist_id")
    public int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    public String title;

    public String startDate;

    public String endDate;

    public String totalDate;


    @OneToMany(mappedBy = "tourList")
    public List<Tour> tour;

    @OneToMany(mappedBy = "tourList")
    public List<Hotel> hotel;
}
