package com.example.camperpro.data.model.responses

import com.example.camperpro.data.model.dto.AdDto

@kotlinx.serialization.Serializable
class AdResponse(
    val ads: List<AdDto>
)