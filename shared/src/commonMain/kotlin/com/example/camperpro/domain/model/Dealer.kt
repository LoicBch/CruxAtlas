package com.example.camperpro.domain.model

import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize
@CommonParcelize
data class Dealer(
    val id: String,
    val name: String,
    val brands: List<String>,
    val services: List<String>,
    val address: String,
    val postalCode: String,
    val countryIso: String,
    val phone: String,
    val email: String,
    val website: String,
    val facebook: String,
    val youtube: String,
    val instagram: String,
    val twitter: String,
    var isPremium: Boolean,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    var photos: List<Photo> = listOf()
) : CommonParcelable


























