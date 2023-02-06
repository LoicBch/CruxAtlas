package com.example.camperpro.domain.model

import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize
@CommonParcelize
data class Dealer(
    var id: String,
    var name: String,
    var brands: List<String>,
    var services: List<String>,
    var address: String,
    var postalCode: String,
    var countryIso: String,
    var phone: String,
    var email: String,
    var website: String,
    var facebook: String,
    var youtube: String,
    var instagram: String,
    var twitter: String,
    var isPremium: Boolean,
    var city: String,
    var latitude: Double,
    var longitude: Double,
    var photos: List<Photo> = listOf()
) : CommonParcelable


























