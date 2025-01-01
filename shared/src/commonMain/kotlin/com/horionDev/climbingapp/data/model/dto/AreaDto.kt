package com.horionDev.climbingapp.data.model.dto

import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.domain.model.composition.Location
import kotlinx.serialization.Serializable

@Serializable
class AreaDto(
    val id: Int,
    val name: String,
    val country: String,
    val polygon: List<Location>
)