package com.horionDev.climbingapp.domain.model.composition

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@kotlinx.serialization.Serializable
@CommonParcelize
class BoundingBox(
    val minLatitude : Double,
    val maxLatitude : Double,
    val minLongitude : Double,
    val maxLongitude : Double,
): CommonParcelable