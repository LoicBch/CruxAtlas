package com.horionDev.climbingapp.domain.model.entities
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.utils.Country

data class Area (
    val id : Int,
    val name : String,
    val country: String,
    val description : String = "",
    val polygon : List<Location> = emptyList(),
)