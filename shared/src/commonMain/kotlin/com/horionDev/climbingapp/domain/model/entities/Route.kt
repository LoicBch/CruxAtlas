package com.horionDev.climbingapp.domain.model.entities

data class Route (
    val id: Int,
    val name: String,
    val description: String,
    val grade: String,
    val area: String,
    val image: String
)