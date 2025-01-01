package com.horionDev.climbingapp.domain.model.composition

import com.horionDev.climbingapp.utils.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.throwMissingFieldException
import kotlin.math.*

@Serializable
class Location(val latitude: Double, val longitude: Double)

fun List<Location>.calculateCentroid(): Location {
    val totalPoints = this.size
    val centroidLatitude = this.sumOf { it.latitude } / totalPoints
    val centroidLongitude = this.sumOf { it.longitude } / totalPoints
    return Location(centroidLatitude, centroidLongitude)
}

val Location.distanceFromUserLocation: Double?
    get() = Globals.GeoLoc.lastKnownLocation?.let {
        this.distanceFrom(it)
    }

fun Location.distanceFromUserLocationText(preference: KMMPreference): String {
    return when (preference.getInt(Constants.PreferencesKey.METRIC, 0)) {
        0 -> {
            Globals.GeoLoc.lastKnownLocation.let {
                "${round(this.distanceFrom(it) * 10.0) / 10.0} km"
            }
        }

        1 -> {
            Globals.GeoLoc.lastKnownLocation.let {
                "${(round(this.distanceFrom(it) * 10.0) / 10.0).fromKmToMiles()} mi"
            }
        }

        else -> {
            Globals.GeoLoc.lastKnownLocation.let {
                "${round(this.distanceFrom(it) * 10.0) / 10.0} km"
            }
        }
    }
}

val Location.distanceFromLastSearch: Double
    get() = Globals.GeoLoc.lastSearchedLocation.let {
        this.distanceFrom(it)
    }

val Location.isAroundLastSearchedLocation
    get() = this.distanceFrom(
        Globals.GeoLoc
            .lastSearchedLocation
    ) <
            Globals.GeoLoc.RADIUS_AROUND_LIMIT

fun Location.distanceFrom(location: Location): Double {
    val earthRadius = 6371.0 // rayon de la Terre en kilomètres

    val lat1 = (this.latitude * PI) / 180
    val lon1 = (this.longitude * PI) / 180
    val lat2 = (location.latitude * PI) / 180
    val lon2 = (location.longitude * PI) / 180

    val deltaLat = lat2 - lat1
    val deltaLon = lon2 - lon1

    val a = sin(deltaLat / 2) * sin(deltaLat / 2) + cos(lat1) * cos(lat2) * sin(deltaLon / 2) * sin(
        deltaLon / 2
    )

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val toKm = earthRadius * c
    return toKm
}