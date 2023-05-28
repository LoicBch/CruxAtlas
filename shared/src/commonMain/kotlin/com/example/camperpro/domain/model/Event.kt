package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

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