package com.jetbrains.kmm.shared.data.model.responses

import com.example.camperpro.data.model.dto.SpotDto
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SpotResponse(

    @SerialName("api_infos")
    val api_infos: String = "",

    @SerialName("status")
    val status: String = "",

    @SerialName("lieux")
    val spots: List<SpotDto> = listOf()

)
