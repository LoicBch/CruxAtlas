package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsItemDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)