package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.data.model.dto.RouteDto
import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize
import kotlinx.serialization.Serializable

@Serializable
@CommonParcelize
class RouteLog(
    var id: Int,
    var userId: Int,
    var routeId: Int,
    var routeName: String,
    var text: String,
    var rating: String,
    var date: String
): CommonParcelable

@Serializable
@CommonParcelize
class RouteWithLog(
    var log: RouteLog,
    var route: RouteDto
): CommonParcelable