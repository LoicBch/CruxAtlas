package com.horionDev.climbingapp.domain.model.composition

@kotlinx.serialization.Serializable
data class AuthRequest(val username: String, val password: String)
