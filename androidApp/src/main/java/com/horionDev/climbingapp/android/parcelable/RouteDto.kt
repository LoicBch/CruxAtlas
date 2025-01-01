package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class RouteDtoParcel(
    val id: Int,
    val cragId: Int,
    val sectorId: Int,
    val name: String,
    val grade: String,
    val ascents: Int? = null,
    val rating: Double? = null,
) : Parcelable