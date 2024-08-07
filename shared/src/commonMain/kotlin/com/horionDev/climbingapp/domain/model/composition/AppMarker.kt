package com.horionDev.climbingapp.domain.model.composition

data class AppMarker(
    val placeLinkedId: String,
    val selected: Boolean = false,
    val latitude: Double,
    val longitude: Double
)
