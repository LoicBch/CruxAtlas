package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelParcel(
    val id: String = "",
    val filePath: String = "",
    val name: String = "",
    val sectorNames: List<String> = emptyList(),
    val routesCount: Int = 0,
    val downloadedDate: String? = null,
    val parentCrag: String = "",
    var size: Long = 0,
    val data: ByteArray? = null
) : Parcelable
