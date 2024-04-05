package com.example.kotrip.entity.tourlist.hotel;

import com.example.kotrip.entity.tourlist.TourList;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hotel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    public int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id")
    public HotelInfo hotelInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourlist_id")
    public TourList tourList;

    public int tripDate;

    public int duration;

    public String date;
}
