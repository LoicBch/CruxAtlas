package com.horionDev.climbingapp.domain.model.composition

import com.horionDev.climbingapp.utils.*
import kotlin.math.*

@CommonParcelize
class Location(val latitude: Double, val longitude: Double) : CommonParcelable

val Location.distanceFromUserLocation: Double?
    get() = Globals.geoLoc.lastKnownLocation?.let {
        this.distanceFrom(it)
    }

fun Location.distanceFromUserLocationText(preference: KMMPreference): String {
    return when (preference.getInt(Constants.PreferencesKey.METRIC, 0)) {
        0 -> {
            Globals.geoLoc.lastKnownLocation.let {
                "${round(this.distanceFrom(it) * 10.0) / 10.0} km"
            }
        }
        1 -> {
            Globals.geoLoc.lastKnownLocation.let {
                "${(round(this.distanceFrom(it) * 10.0) / 10.0).fromKmToMiles()} mi"
            }
        }
        else -> {
            Globals.geoLoc.lastKnownLocation.let {
                "${round(this.distanceFrom(it) * 10.0) / 10.0} km"
            }
        }
    }
}

val Location.distanceFromLastSearch: Double
    get() = Globals.geoLoc.lastSearchedLocation.let {
        this.distanceFrom(it)
    }

val Location.isAroundLastSearchedLocation get() = this.distanceFrom(Globals.geoLoc
                                                                        .lastSearchedLocation) <
        Globals.geoLoc.RADIUS_AROUND_LIMIT

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