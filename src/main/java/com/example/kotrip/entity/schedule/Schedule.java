package com.example.kotrip.entity.schedule;

import com.example.kotrip.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Schedule {

    @Id
    @Column(name = "schedule_id")
    private String id;

    private String title;

    private Integer areaId;

    private LocalDate time;

    private String classificationId;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "schedule", cascade = CascadeType.REMOVE)
    List<ScheduleTour> tours = new ArrayList<>();


    public static Schedule toEntity(String title, String classificationId, int areaId, LocalDate time, User user, List<ScheduleTour> tours, String id) {
        return Schedule.builder()
                .title(title)
                .classificationId(classificationId)
                .areaId(areaId)
                .time(time)
                .user(user)
                .tours(tours)
                .id(id)
                .build();
    }
}
