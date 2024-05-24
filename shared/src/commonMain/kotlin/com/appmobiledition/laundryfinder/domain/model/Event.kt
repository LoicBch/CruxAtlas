package com.appmobiledition.laundryfinder.domain.model

import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize

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