package com.horionDev.climbingapp.data.model.dto

import com.horionDev.climbingapp.domain.model.composition.BoundingBox

class AreaDto(
    val id: Int,
    val name: String,
    val boundingBox: BoundingBox = BoundingBox(0.0, 0.0, 0.0, 0.0)
)