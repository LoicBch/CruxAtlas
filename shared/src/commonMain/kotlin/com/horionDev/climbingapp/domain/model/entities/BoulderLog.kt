package com.horionDev.climbingapp.domain.model.entities

import kotlinx.serialization.Serializable

@Serializable
class BoulderLog(
    var id: Int,
    var userId: Int,
    var boulderId: Int,
    var text: String,
    var rating: String,
    var logDate: String
)