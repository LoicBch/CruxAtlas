package com.horionDev.climbingapp.domain.model

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@CommonParcelize
class Ad(
    val type: String,
    val url: String,
    val redirect: String,
    val click: String
): CommonParcelable