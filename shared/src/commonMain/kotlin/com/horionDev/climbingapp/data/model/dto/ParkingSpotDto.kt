package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParkingSpotDto(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)
