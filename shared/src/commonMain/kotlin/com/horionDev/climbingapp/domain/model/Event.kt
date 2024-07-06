package com.horionDev.climbingapp.domain.model

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@CommonParcelize
class Event(
    val id: String,
    val name: String,
    val descriptionFr: String,
    val descriptionEn: String,
    val descriptionEs: String,
    val descriptionDe: String,
    val descriptionNl: String,
    val dateBegin: String,
    val dateEnd: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val postalCode: String,
    val city: String,
    val country: String,
    val countryIso: String,
    var photos: List<Photo>,
    val website: String
) : CommonParcelable