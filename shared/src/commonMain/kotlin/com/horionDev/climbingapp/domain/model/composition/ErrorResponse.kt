package com.horionDev.climbingapp.domain.model.composition

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val response: String
)