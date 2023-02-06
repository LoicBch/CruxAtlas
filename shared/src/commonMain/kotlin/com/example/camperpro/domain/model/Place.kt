package com.example.camperpro.domain.model

import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.utils.CommonParcelable
import com.example.camperpro.utils.CommonParcelize

@CommonParcelize
data class Place(
    val name: String,
    val location: Location
) : CommonParcelable