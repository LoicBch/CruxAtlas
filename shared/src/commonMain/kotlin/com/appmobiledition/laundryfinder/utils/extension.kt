package com.appmobiledition.laundryfinder.utils

import com.appmobiledition.laundryfinder.data.model.dto.LaundryDto
import com.appmobiledition.laundryfinder.domain.model.Event
import com.appmobiledition.laundryfinder.domain.model.Dealer
import kotlin.math.floor
import kotlin.math.round

fun String.toBool() = this.lowercase() == "true" || this == "1"
fun Double.toDMS(): String {
    val degrees = floor(this).toInt()
    val minutes = floor((this - degrees) * 60).toInt()
    val seconds = round((this - degrees - (minutes / 60.0)) * 3600 * 1000) / 1000.0

    return "$degrees° $minutes' $seconds''"
}


//Faire un objet location et mettre cette extension sur location
//val LaundryDto.fullLocation get() = "$ville, $code_postal"
val LaundryDto.fullLocation get() = "climbing address"
val LaundryDto.fullGeolocalisation
    get() = "$latitude, $longitude (lat, lng)\nN ${
        latitude!!.toDouble()
            .toDMS()
    }, E ${longitude!!.toDouble().toDMS()}"

val Dealer.fullLocation get() = "$address, $city, $postalCode"
val Dealer.fullGeolocalisation
    get() =
        "$latitude, $longitude (lat, lng)\nN ${latitude.toDMS()}, E ${longitude.toDMS()}"

val Event.fullLocation get() = "$address, $city, $postalCode"
val Event.fullGeolocalisation
    get() =
        "$latitude, $longitude (lat, lng)\nN ${latitude.toDMS()}, E ${longitude.toDMS()}"