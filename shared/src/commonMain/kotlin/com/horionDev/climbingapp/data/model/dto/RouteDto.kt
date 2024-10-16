package com.horionDev.climbingapp.data.model.dto

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize
import kotlinx.serialization.Serializable

@Serializable
@CommonParcelize
class RouteDto(
    val id: Int,
    val cragId: Int,
    val cragName: String,
    val sectorId: Int,
    val name: String,
    val grade: String
): CommonParcelable