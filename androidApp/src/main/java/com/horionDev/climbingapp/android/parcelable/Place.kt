package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceParcel(
    val name: String,
    val location: LocationParcel
) : Parcelable
