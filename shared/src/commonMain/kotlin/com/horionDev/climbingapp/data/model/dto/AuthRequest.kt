package com.horionDev.climbingapp.data.model.dto

@kotlinx.serialization.Serializable
data class AuthRequest(val username: String, val password: String)
