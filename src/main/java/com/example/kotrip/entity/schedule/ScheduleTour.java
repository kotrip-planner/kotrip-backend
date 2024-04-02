package com.example.kotrip.entity.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ScheduleTour {

    @Id @GeneratedValue
    @Column(name = "tour_id")
    private Long id;

    private String title;

    private Long duration;

    private String imageUrl;

    private Double mapX;

    private Double mapY;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}
