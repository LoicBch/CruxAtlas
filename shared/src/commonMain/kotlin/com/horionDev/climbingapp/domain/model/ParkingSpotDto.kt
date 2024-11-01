package com.horionDev.climbingapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ParkingSpotDto(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)