package com.horionDev.climbingapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
class RouteLogDto(
    var id: Int,
    var userId: Int,
    var routeId: Int,
    var routeName: String,
    var text: String,
    var rating: String,
    var date: String
)
