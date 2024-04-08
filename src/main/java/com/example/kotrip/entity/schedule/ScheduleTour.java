package com.example.kotrip.entity.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTour {

    @Id @GeneratedValue
    @Column(name = "tour_id")
    private Long id;

    private String title;

    private Long duration;

    private String imageUrl;

    private BigDecimal mapX;

    private BigDecimal mapY;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public static ScheduleTour toEntity(Long id, String title, Long duration, String imageUrl, BigDecimal mapX, BigDecimal mapY){
        return ScheduleTour.builder()
                .id(id)
                .title(title)
                .duration(duration)
                .imageUrl(imageUrl)
                .mapX(mapX)
                .mapY(mapY)
                .build();
    }
}