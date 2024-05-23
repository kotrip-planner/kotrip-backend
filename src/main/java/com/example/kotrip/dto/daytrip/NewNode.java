package com.example.kotrip.dto.daytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewNode {
    private Long startId;
    private String startName;
    private Double startLatitude;
    private Double startLongitude;
    private Long destId;
    private String destName;
    private Double destLatitude;
    private Double destLongitude;
}
