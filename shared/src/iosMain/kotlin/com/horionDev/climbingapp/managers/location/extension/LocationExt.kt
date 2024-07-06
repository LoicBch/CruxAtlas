package com.horionDev.climbingapp.managers.location.extension

import com.horionDev.climbingapp.managers.location.BaseLocationManagerCompanion
import com.horionDev.climbingapp.managers.location.LocationAuthorizationStatus
import com.horionDev.climbingapp.managers.location.LocationManager

typealias OnAlwaysAllowsPermissionRequiredBlock = () -> Unit

var LocationManager.Companion.requiredPermission: LocationAuthorizationStatus
    get() = locationLoyal.requiredPermission.value
    set(value) {
        locationLoyal.requiredPermission.value = value
    }

fun LocationManager.Companion.onAlwaysAllowsPermissionRequired(
    target: Any,
    block: OnAlwaysAllowsPermissionRequiredBlock
): BaseLocationManagerCompanion {
    locationLoyal.onAlwaysAllowsPermissionRequired(target, block)
    return this
}

fun LocationManager.Companion.removeOnAlwaysAllowsPermissionRequired(target: Any) =
    locationLoyal.removeOnAlwaysAllowsPermissionRequired(target)

internal var LocationManager.Companion.previousAuthorizationStatus: LocationAuthorizationStatus
    get() = locationLoyal.previousAuthorizationStatus.value
    set(value) {
        locationLoyal.previousAuthorizationStatus.value = value
    }
