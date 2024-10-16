package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.data.model.dto.BoulderDto
import kotlinx.serialization.Serializable

@Serializable
class BoulderLog(
    var id: Int,
    var userId: Int,
    var boulderId: Int,
    var boulderName: String,
    var text: String,
    var rating: String,
    var logDate: String
)

@Serializable
class BoulderWithLog(
    var log: BoulderLog,
    var boulder: BoulderDto
)