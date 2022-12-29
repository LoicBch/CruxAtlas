package com.example.camperpro.data.model.dto

data class SearchDto(
    val id: Long,
    val categoryKey: String,
    val searchLabel: String,
    val timeStamp: Long
)

data class LocationSearchDto(
    val id : Long,
    val label: String,
    val timeStamp: Long,
    val lat: Double,
    val lon: Double
)