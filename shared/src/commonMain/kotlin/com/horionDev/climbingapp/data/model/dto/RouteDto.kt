package com.horionDev.climbingapp.data.model.dto
import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    val id: Int,
    val cragId: Int,
    val cragName: String? = null,
    val sectorId: Int,
    val name: String,
    val grade: String,
    val sectorName : String? = null,
    val ascents: Int? = null,
    val rating: Double? = null,
)