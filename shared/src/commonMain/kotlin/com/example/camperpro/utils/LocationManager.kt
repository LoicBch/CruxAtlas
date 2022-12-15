package com.example.camperpro.utils

import com.example.camperpro.domain.model.Location


expect class LocationManager(context: Any?){
    fun startLocationObserver()
    fun stopLocationObserver()
    fun getCurrentLocation(doWithLocation: (Location) -> Unit)
}