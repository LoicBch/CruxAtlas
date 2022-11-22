package com.example.camperpro.domain.model

import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

@CommonParcelize
data class Spot(
    var name: String,
    var description: String,
    var creatorName: String,
    var lat: Double,
    var long: Double,
    var photos: List<Photo>
) : CommonParcelable



























