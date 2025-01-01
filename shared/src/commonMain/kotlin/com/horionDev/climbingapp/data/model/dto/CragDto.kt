package com.horionDev.climbingapp.data.model.dto

import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.domain.model.entities.Sector
import kotlinx.serialization.Serializable

@Serializable
class CragDto(
    val id: Int,
    val areaId: String,
    val name: String,
    val description: String? = "",
    val sectors: List<SectorDto>? = emptyList(),
    val latitude: Double,
    val longitude: Double,
    val thumbnailUrl: String? = "",
    val boundingBox: BoundingBox? = BoundingBox(0.0, 0.0, 0.0, 0.0),
    val orientation: String? = "",
    val altitude: String? = "",
    val approachLenght: String? = ""
)

