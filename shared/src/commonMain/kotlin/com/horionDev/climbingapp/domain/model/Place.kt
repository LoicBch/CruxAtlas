package com.horionDev.climbingapp.domain.model

import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@CommonParcelize
data class Place(
    val name: String,
    val location: Location
) : CommonParcelable