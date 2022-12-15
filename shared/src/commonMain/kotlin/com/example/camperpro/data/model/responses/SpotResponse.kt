package com.example.camperpro.data.model.responses

import com.example.camperpro.data.model.dto.SpotDto
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SpotResponse(

    @SerialName("status")
    val status: String = "",

    @SerialName("dealers")
    val spots: List<SpotDto> = listOf()

)
