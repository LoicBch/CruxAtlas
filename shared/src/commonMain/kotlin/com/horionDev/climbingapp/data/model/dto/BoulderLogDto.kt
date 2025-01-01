package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
class BoulderLogDto(
    var id: Int,
    var userId: Int,
    var boulderId: Int,
    var boulderName: String,
    var text: String,
    var rating: String,
    var logDate: String
)