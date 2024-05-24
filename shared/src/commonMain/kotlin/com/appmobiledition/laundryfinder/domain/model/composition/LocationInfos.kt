package com.appmobiledition.laundryfinder.domain.model.composition

import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize

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