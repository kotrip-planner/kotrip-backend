package com.example.kotrip.dto

data class NewNode(
    val startId: Int,
    val startName: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val destId: Int,
    val destName: String,
    val destLatitude: Double,
    val destLongitude: Double,
    val time: Int
)
