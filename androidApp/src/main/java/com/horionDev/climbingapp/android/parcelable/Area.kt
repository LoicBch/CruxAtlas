package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import com.horionDev.climbingapp.domain.model.composition.Location
import kotlinx.parcelize.Parcelize

@Parcelize
data class AreaParcel(
    val id: Int,
    val name: String,
    val description: String = "",
    val polygon: List<Pair<Double, Double>> = emptyList(),
    val crags: List<CragParcel> = emptyList()
) : Parcelable