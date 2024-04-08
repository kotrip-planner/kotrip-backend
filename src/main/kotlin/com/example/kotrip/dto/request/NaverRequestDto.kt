package com.example.kotrip.naver

import com.example.kotrip.dto.Node
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import java.time.LocalDate

@Getter
data class NaverRequestDto(
    @JsonProperty("kotrip") val kotrip: List<NaverKotripDto>
)

@Getter
data class NaverKotripDto(
    val date:LocalDate,
    val nodes: List<Node>
)


