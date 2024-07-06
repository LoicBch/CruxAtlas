package com.horionDev.climbingapp.domain.model.composition

data class Marker(
    val placeLinkedId: String,
    val selected: Boolean = false,
    val latitude: Double,
    val longitude: Double
)
