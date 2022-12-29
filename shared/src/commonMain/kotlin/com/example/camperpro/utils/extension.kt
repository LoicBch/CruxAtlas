package com.example.camperpro.utils

import com.example.camperpro.domain.model.Spot
import kotlin.math.floor
import kotlin.math.round

fun String.toBool() = this.lowercase() == "true" || this == "1"
fun Double.toDMS(): String {
    val degrees = floor(this).toInt()
    val minutes = floor((this - degrees) * 60).toInt()
    val seconds = round((this - degrees - (minutes / 60.0)) * 3600 * 1000) / 1000.0

    return "$degrees° $minutes' $seconds''"
}


val Spot.fullLocation get() = "$address, $city, $postalCode"
val Spot.fullGeolocalisation
    get() =
        "$latitude, $longitude (lat, lng)\rN ${latitude.toDMS()}, E ${longitude.toDMS()}"

