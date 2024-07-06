package com.horionDev.climbingapp.data.model.responses

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LocationInfoResponse(
    val lat: Double,
    val lon: Double,
    val country: String,
    val iso: String,
    val address: String,

    @SerialName("gps_deci_txt")
    val gpsDeciTxt: String,

    @SerialName("gps_dms_txt")
    val gpsDmsTxt: String
)