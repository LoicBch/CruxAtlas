package com.appmobiledition.laundryfinder.data.model.responses

import com.appmobiledition.laundryfinder.data.model.dto.PartnerDto
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PartnerResponse(

    @SerialName("status")
    val status: String = "",

    @SerialName("dealers")
    val partners: List<PartnerDto> = listOf()
)