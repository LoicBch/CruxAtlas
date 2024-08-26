package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
class SectorDto(
    val id: Int,
    val cragId: Int,
    val name: String
)