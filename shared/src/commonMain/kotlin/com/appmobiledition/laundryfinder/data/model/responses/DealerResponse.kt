package com.appmobiledition.laundryfinder.data.model.responses

import com.appmobiledition.laundryfinder.data.model.dto.DealerDto
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DealerResponse(

    @SerialName("status")
    val status: String = "",

    @SerialName("dealers")
    val dealers: List<DealerDto> = listOf()

)
