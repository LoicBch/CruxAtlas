package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.domain.model.composition.BoundingBox

data class Area (
    val id : Int,
    val name : String,
    val description : String,
    val boundingBox : BoundingBox,
)