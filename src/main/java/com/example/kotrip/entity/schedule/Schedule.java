package com.example.kotrip.entity.schedule;

import com.example.kotrip.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class Schedule {

    @Id @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    private Integer areaId;

    private LocalDate time;

    private String classificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule")
    List<ScheduleTour> tours = new ArrayList<>();


    public static Schedule toEntity(String classificationId, int areaId, LocalDate time, User user,List<ScheduleTour> tours) {
        return Schedule.builder()
                .classificationId(classificationId)
                .areaId(areaId)
                .time(time)
                .user(user)
                .tours(tours)
                .build();
    }
}
