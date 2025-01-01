package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class RouteLogParcel(
    var id: Int,
    var userId: Int,
    var routeId: Int,
    var routeName: String,
    var text: String,
    var rating: String,
    var date: String
): Parcelable

@Parcelize
class RouteWithLogParcel(
    var log: RouteLogParcel,
    var route: RouteDtoParcel
): Parcelable