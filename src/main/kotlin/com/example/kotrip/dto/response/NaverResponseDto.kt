package com.example.kotrip.naver

data class NaverResponseDto(
    val code: Int,
    val message: String,
    val route: NaverRouteResponseDto?
)

data class NaverRouteResponseDto(
    val trafast: List<NaverSummaryResponseDto>
)

data class NaverSummaryResponseDto(
    val summary: NaverDurationResponseDto
)

data class NaverDurationResponseDto(
    val duration: Long
)
