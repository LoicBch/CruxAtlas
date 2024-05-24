package com.appmobiledition.laundryfinder.data.model.dto

data class SearchDto(
    val categoryKey: String,
    val searchLabel: String,
    val timeStamp: Long
)

data class LocationSearchDto(
    val label: String,
    val timeStamp: Long,
    val lat: Double,
    val lon: Double
)

