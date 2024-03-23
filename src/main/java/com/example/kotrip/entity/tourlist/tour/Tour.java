package com.example.kotrip.entity.tourlist.tour;

import com.example.kotrip.entity.tourlist.TourList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    public int id;

    @OneToOne
    @JoinColumn(name = "info_id")
    public TourInfo tourInfo;

    @ManyToOne
    @JoinColumn(name = "tourlist_id")
    public TourList tourList;

    public int tourOrder;

    public int tripDate;

    public int duration;

    public String date;


}
