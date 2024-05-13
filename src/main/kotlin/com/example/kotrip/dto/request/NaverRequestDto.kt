package com.example.kotrip.naver

import com.example.kotrip.dto.Node
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import java.time.LocalDate

@Getter
data class NaverRequestDto( // 1개의 여행 스케줄
    @JsonProperty("title") val title: String,
    @JsonProperty("kotrip") val kotrip: List<NaverKotripDto>,
    @JsonProperty("areaId") val areaId: Int
)

@Getter
data class NaverKotripDto( // 여행 1일차, 2일차..
    val date:LocalDate,
    val nodes: List<Node>
)