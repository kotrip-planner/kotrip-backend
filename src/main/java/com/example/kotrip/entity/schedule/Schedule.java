package com.example.kotrip.entity.schedule;

import com.example.kotrip.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private LocalDate time;

    private String classificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "schedule")
    List<ScheduleTour> tours = new ArrayList<>();


    public static Schedule toEntity(String classificationId, LocalDate time, User user,List<ScheduleTour> tours) {
        return Schedule.builder()
                .classificationId(classificationId)
                .time(time)
                .user(user)
                .tours(tours)
                .build();
    }
}
