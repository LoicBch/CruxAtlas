package com.example.camperpro.utils

import com.example.camperpro.domain.model.Location
import kotlinx.coroutines.flow.Flow


expect class LocationClient(context: Any?){
    fun startLocationObserver()
    fun stopLocationObserver()
    fun getCurrentLocation(doWithLocation: (Location) -> Unit)
    fun getLocationUpdates(interval: Long): Flow<Location>
    class LocationException(message: String): Exception
}