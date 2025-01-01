package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SectorParcel(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val cragId: Int = 0,
    val routes: List<RouteParcel> = emptyList(),
    val parkingSpots: List<ParkingSpotDtoParcel>? = emptyList(),
    val boundingBox: BoundingBoxParcel = BoundingBoxParcel(0.0, 0.0, 0.0, 0.0),
    val image: String = ""
): Parcelable