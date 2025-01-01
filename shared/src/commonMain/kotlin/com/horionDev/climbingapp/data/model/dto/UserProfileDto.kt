package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val username: String,
    val email: String,
    val creationDate: String,
)