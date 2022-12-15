package com.example.camperpro.data.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SpotDto(

    var id: String? = null,

    @SerialName("nom")
    var name: String = "",
    var distance: String = "",

    @SerialName("concessionnaire")
    var dealer: String = "",

    @SerialName("loueur")
    var renter: String? = null,
    var garage: String? = null,

    @SerialName("boutique")
    var shop: String? = null,

    @SerialName("marques")
    var brand: String? = null,
    var services: String? = null,

    @SerialName("geolocalisable")
    var geolocatable: String? = null,
    var latitude: String = "",
    var longitude: String = "",

    @SerialName("adresse")
    var adress: String? = null,

    @SerialName("code_postal")
    var zipcode: String? = null,

    @SerialName("commune")
    var city: String? = null,

    @SerialName("pays")
    var country: String? = null,

    @SerialName("pays_iso")
    var countryIso: String? = null,

    @SerialName("tel")
    var phone: String? = null,
    var email: String? = null,

    @SerialName("site_internet")
    var website: String? = null,
    var facebook: String? = null,
    var youtube: String? = null,
    var instagram: String? = null,
    var twitter: String? = null,
    var premium: String? = null,
    var photos: List<PhotoDto> = listOf()
)

@kotlinx.serialization.Serializable
data class PhotoDto(var photo: String? = null)