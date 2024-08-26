package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val isSubscribe: Boolean
)
