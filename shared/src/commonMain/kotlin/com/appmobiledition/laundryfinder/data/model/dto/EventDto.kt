package com.appmobiledition.laundryfinder.data.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class EventDto(

    var id: String = "",
    var name: String = "",

    @SerialName("description_fr")
    var descriptionFr: String = "",

    @SerialName("description_en")
    var descriptionEn: String = "",

    @SerialName("description_es")
    var descriptionEs: String = "",

    @SerialName("description_de")
    var descriptionDe: String = "",

    @SerialName("description_nl")
    var descriptionNl: String = "",

    @SerialName("date_begin")
    var dateBegin: String = "",

    @SerialName("date_end")
    var dateEnd: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var address: String = "",

    @SerialName("postal_code")
    var postalCode: String = "",
    var city: String = "",
    var country: String = "",

    @SerialName("country_iso")
    var countryIso: String = "",

    @SerialName("image_url")
    var imageUrl: String = "",
    var url: String = ""
)