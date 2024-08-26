package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
class RouteDto(
    val id: Int,
    val cragId: Int,
    val sectorId: Int,
    val name: String,
    val grade: String
)