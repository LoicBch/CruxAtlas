package com.appmobiledition.laundryfinder.managers.location.extension

import android.app.Activity
import android.content.Context
import android.location.Location
import com.appmobiledition.laundryfinder.managers.location.AndroidLocationRequest
import com.appmobiledition.laundryfinder.managers.location.Coordinates
import com.appmobiledition.laundryfinder.managers.location.LocationData
import com.appmobiledition.laundryfinder.managers.location.LocationManager
import java.lang.ref.WeakReference

fun LocationManager.Companion.processRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
) = locationLoyal.processRequestPermissionsResult(
    requestCode,
    permissions,
    grantResults
)

fun LocationManager.Companion.setLocationRequest(androidLocationRequest: AndroidLocationRequest) =
    locationLoyal.setLocationRequest(androidLocationRequest)


internal var LocationManager.Companion.activity: Activity?
    get() = locationLoyal.activity?.get()
    set(value) {
        locationLoyal.activity = WeakReference(value)
    }

internal fun LocationManager.Companion.configure(context: Context) =
    locationLoyal.configure(context)

fun Location.toLocationData(): LocationData = LocationData(
    accuracy.toDouble(),
    altitude,
    0.0,
    bearing.toDouble(),
    speed.toDouble(),
    Coordinates(latitude, longitude)
)