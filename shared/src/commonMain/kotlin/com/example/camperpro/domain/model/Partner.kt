package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

// This domain object is not necesssary, optimal choice would be to NOT have a webservice to get partner but just add an extension function "isPartner" to Dealer objets that check
// the isGeolocatible of Dealer object. because a partner IS a dealer.. and dealer have a goelocatible that shoudnt not be there, exept from this property they are exactly the same objects.
// But Dealer.php doesnt get the Partner so we have to make an other webService thus an other object to keep consistensy

@CommonParcelize
class Partner(
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
    var photos: List<Photo> = listOf()
) : CommonParcelable