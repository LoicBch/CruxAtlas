package com.appmobiledition.laundryfinder.domain.model

import com.appmobiledition.laundryfinder.utils.CommonParcelable
import com.appmobiledition.laundryfinder.utils.CommonParcelize

// This domain object is not necesssary, optimal choice would be to NOT have a webservice to get partner but just add an extension function "isPartner" to Dealer objets that check
// the isGeolocatible of Dealer object. because a partner IS a dealer.. and dealer have a goelocatible that shoudnt not be there, exept from this property they are exactly the same objects.
// But Dealer.php doesnt get the Partner so we have to make an other webService thus an other object to keep consistensy

@CommonParcelize
class Partner(
    val id: String,
    val name: String,
    val description: String,
    val brands: List<String>,
    val services: List<String>,
    val phone: String,
    val email: String,
    val website: String,
    val facebook: String,
    val youtube: String,
    val instagram: String,
    val twitter: String,
    val isPremium: Boolean,
    val photos: List<Photo> = listOf()
) : CommonParcelable