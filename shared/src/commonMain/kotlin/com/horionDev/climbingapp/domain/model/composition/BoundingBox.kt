package com.horionDev.climbingapp.domain.model.composition

@kotlinx.serialization.Serializable
class BoundingBox(
    val minLatitude : Double,
    val maxLatitude : Double,
    val minLongitude : Double,
    val maxLongitude : Double,
)
