package com.horionDev.climbingapp.domain.model.entities

import kotlinx.serialization.Serializable

@Serializable
class RouteLog(
    var id: Int,
    var userId: Int,
    var routeId: Int,
    var text: String,
    var rating: String,
    var date: String
)