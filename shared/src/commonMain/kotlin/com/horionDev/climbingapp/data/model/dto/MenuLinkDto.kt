package com.horionDev.climbingapp.data.model.dto

@kotlinx.serialization.Serializable
class MenuLinkDto(
    val id: Int,
    val name: String,
    val subtitle: String,
    val icon: String,
    val url: String,
    val urlstat: String,
)