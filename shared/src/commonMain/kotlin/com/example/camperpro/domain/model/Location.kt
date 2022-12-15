package com.example.camperpro.domain.model

import com.example.camperpro.utils.Globals
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class Location(val latitude: Double, val longitude: Double)

fun Location.distanceFromUserLocation(): Double? = Globals.lastKnownLocation?.let {
    this.distanceFrom(it) }


fun Location.distanceFrom(location: Location): Double {
    val lat1 = this.latitude
    val lat2 = this.longitude
    val lon1 = location.latitude
    val lon2 = location.longitude

    val theta = lon1 - lon2
    var dist =
        sin(lat1.degreeToRadius()) * sin(lat2.degreeToRadius()) + cos(lat1.degreeToRadius()) * cos(
            lat2.degreeToRadius()
        ) * cos(
            theta.degreeToRadius()
        )
    dist = acos(dist)
    dist = dist.radiusToDegree()
    dist *= 60 * 1.1515
    dist *= 1.609344
    return dist
}

fun Double.degreeToRadius() = this * PI / 180.0
fun Double.radiusToDegree() = this * 180.0 / PI