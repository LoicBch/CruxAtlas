package com.appmobiledition.laundryfinder.data.model.responses

import com.appmobiledition.laundryfinder.data.model.dto.AdDto

@kotlinx.serialization.Serializable
class AdResponse(
    val ads: List<AdDto>
)