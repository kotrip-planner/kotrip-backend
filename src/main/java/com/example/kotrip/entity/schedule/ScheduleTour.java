package com.example.kotrip.entity.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTour {

    @Id @GeneratedValue
    @Column(name = "tour_id")
    private Long id;

    private String title;

    private Long duration;

    private String imageUrl;

    private Double mapX;

    private Double mapY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public ScheduleTour setSchedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public static ScheduleTour toEntity(Long id, String title, Long duration, String imageUrl, double mapX, double mapY, Schedule schedule){

        return ScheduleTour.builder()
                .id(id)
                .title(title)
                .duration(duration)
                .imageUrl(imageUrl)
                .mapX(mapX)
                .mapY(mapY)
                .schedule(schedule)
                .build();
    }
}
