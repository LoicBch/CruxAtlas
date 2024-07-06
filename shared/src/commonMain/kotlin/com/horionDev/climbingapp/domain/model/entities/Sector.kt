package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.domain.model.composition.BoundingBox

data class Sector(
    val id: Int,
    val name: String,
    val description: String,
    val cragId: Int,
    val routes: List<Route>,
    val boundingBox: BoundingBox
)