package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.domain.model.entities.Sector
import kotlinx.parcelize.Parcelize


@Parcelize
data class CragParcel(
    val id: Int = 0,
    val name: String = "",
    val description: String? = "",
    val areaId: Int = 0,
    val sectors: List<SectorParcel> = emptyList(),
    val latitude: Double = 44.51,
    val longitude: Double = 5.93,
    val image: String? = "",
    val orientation: String? = "South",
    val altitude: String? = "2016m",
    val approachLenght: String? = "30min",
    val boundingBox: BoundingBoxParcel = BoundingBoxParcel(0.0, 0.0, 0.0, 0.0),
): Parcelable