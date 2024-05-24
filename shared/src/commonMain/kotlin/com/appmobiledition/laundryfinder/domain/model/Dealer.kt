package com.appmobiledition.laundryfinder.domain.model

import com.appmobiledition.laundryfinder.domain.model.composition.Marker
import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize
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


























