package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val country: String = "",
    val city: String = "",
    val gender: String = "",
    val age: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val climbingSince: String = "",
    val imageUrl: String? = null,
    val isSubscribe: Boolean = false
)
