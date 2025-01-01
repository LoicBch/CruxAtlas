package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParkingSpotDtoParcel(
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable