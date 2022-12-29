package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.Globals
import kotlin.math.*

@CommonParcelize
class Location(val latitude: Double, val longitude: Double) : CommonParcelable

// TODO: check if location is null
val Location.distanceFromUserLocation: Double?
    get() = Globals.geoLoc.lastKnownLocation?.let {
        this.distanceFrom(it)
    }

val Location.isAroundLastSearchedLocation get() = this.distanceFrom(Globals.geoLoc.lastSearchedLocation!!) < Constants.RADIUS_AROUND_LIMIT

fun Location.distanceFrom(location: Location): Double {
    val earthRadius = 6371.0 // rayon de la Terre en kilomètres

    val lat1 = (this.latitude * PI) / 180
    val lon1 = (this.longitude * PI) / 180
    val lat2 = (location.latitude * PI) / 180
    val lon2 = (location.longitude * PI) / 180

    val deltaLat = lat2 - lat1
    val deltaLon = lon2 - lon1

    val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
            cos(lat1) * cos(lat2) *
            sin(deltaLon / 2) * sin(deltaLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val toKm = earthRadius * c
    return toKm
}

fun Double.degreeToRadius() = this * PI / 180.0
fun Double.radiusToDegree() = this * 180.0 / PI