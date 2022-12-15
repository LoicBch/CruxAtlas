package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

@CommonParcelize
data class Spot(
    var name: String,
    var city: String,
    var latitude: Double,
    var longitude: Double,
) : CommonParcelable



























