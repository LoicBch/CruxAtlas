package com.horionDev.climbingapp.data.model.responses

import com.horionDev.climbingapp.data.model.dto.UserDto

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String,
    val user: UserDto
)