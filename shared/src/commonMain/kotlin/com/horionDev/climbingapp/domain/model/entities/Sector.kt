package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.domain.model.ParkingSpotDto
import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@kotlinx.serialization.Serializable
@CommonParcelize
data class Sector(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val cragId: Int = 0,
    val routes: List<Route> = emptyList(),
    val parkingSpots: List<ParkingSpotDto>? = emptyList(),
    val boundingBox: BoundingBox = BoundingBox(0.0, 0.0, 0.0, 0.0),
    val image: String = ""
): CommonParcelable