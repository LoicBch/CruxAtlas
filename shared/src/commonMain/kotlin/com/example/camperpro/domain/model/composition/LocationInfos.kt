package com.example.camperpro.domain.model.composition

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

@CommonParcelize
data class LocationInfos(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val country: String = "",
    val iso: String = "",
    val address: String = "",
    val gpsDeciTxt: String = "",
    val gpsDmsTxt: String = ""
) : CommonParcelable