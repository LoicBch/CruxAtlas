package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

@CommonParcelize
class Event(
    var id: String,
    var name: String,
    var descriptionFr: String,
    var descriptionEn: String,
    var descriptionEs: String,
    var descriptionDe: String,
    var descriptionNl: String,
    var dateBegin: String,
    var dateEnd: String,
    var latitude: Double,
    var longitude: Double,
    var address: String,
    var postalCode: String,
    var city: String,
    var country: String,
    var countryIso: String,
    var website: String
) : CommonParcelable