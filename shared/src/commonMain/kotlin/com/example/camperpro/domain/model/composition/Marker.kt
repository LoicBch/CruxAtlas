package com.example.camperpro.domain.model.composition

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

// Make this an interface with on implementation per content type if one day  we want to have properties that change in correlation with the type of "content" (eg drawable of the pin)
// this should be T: CommonParcelize but it cant be resolved on android side because on android side if we mention that type expect a commonParcelable it then expect the parcelable class wich is not force equals to CommonParcelable

data class Marker<T>(
    val latitude: Double, val longitude: Double, val content: T
)