package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
class BoulderDto(
    var id: Int,
    var cragId: Int,
    var cragName: String,
    var sectorId: Int,
    var name: String,
    var grade: String,
)