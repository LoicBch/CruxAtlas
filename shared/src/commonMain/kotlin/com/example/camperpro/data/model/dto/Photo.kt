package com.jetbrains.kmm.shared.data.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PhotoDto(
    var id: String? = null,

    @SerialName("link_large")
    var linkLarge: String? = null,

    @SerialName("link_thumb")
    var linkThumb: String? = null,
    var numero: String? = null,

    @SerialName("p4n_user_id")
    var p4nUserId: String? = null,

    @SerialName("pn_lieu_id")
    var pnLieuId: String? = null
)
