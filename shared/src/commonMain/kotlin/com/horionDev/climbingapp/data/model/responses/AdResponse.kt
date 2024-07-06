package com.horionDev.climbingapp.data.model.responses

import com.horionDev.climbingapp.data.model.dto.AdDto

@kotlinx.serialization.Serializable
class AdResponse(
    val ads: List<AdDto>
)