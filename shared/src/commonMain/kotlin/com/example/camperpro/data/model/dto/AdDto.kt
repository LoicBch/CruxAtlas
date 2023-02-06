package com.example.camperpro.data.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class AdDto(
    val id: Int,
    val type: String,
    val url: String,
    val redirect: String,
    val click: String,

    @SerialName("with")
    val width: Int,
    val height: Int
)