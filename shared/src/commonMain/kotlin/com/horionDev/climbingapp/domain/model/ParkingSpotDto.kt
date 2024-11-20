package com.horionDev.climbingapp.domain.model

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize
import kotlinx.serialization.Serializable

@Serializable
@CommonParcelize
data class ParkingSpotDto(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
) : CommonParcelable