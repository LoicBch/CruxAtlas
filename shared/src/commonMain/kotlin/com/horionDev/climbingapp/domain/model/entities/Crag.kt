package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.domain.model.composition.BoundingBox

class Crag(
    val id: Int,
    val name: String,
    val description: String,
    val areaId: Int,
    val sectors: List<Sector>,
    val boundingBox: BoundingBox,
    val image: String
)