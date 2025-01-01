package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import com.horionDev.climbingapp.data.model.dto.SectorDto
import kotlinx.parcelize.Parcelize

@Parcelize
class CradDetailsParcel(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val thumbnailUrl: String,
    val sectors: List<SectorParcel>,
    val models: List<ModelParcel>
) : Parcelable

