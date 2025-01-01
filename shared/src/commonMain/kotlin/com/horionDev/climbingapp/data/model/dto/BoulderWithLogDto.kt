package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
class BoulderWithLogDto(
    var log: BoulderLogDto,
    var boulder: BoulderDto
)