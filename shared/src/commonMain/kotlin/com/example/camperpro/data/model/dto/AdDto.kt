package com.example.camperpro.data.model.dto

@kotlinx.serialization.Serializable
class AdDto(
    val id: Int,
    val type: String,
    val url: String,
    val click: String
)