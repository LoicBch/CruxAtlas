package com.example.camperpro.android.myLocation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.managers.location.LocationManager
import kotlinx.coroutines.flow.MutableStateFlow

class MyLocationViewModel : ViewModel() {

    private var currentLocation = mutableStateOf(Marker("current", false, 0.0, 0.0))
    private val _currentLocation = MutableStateFlow(currentLocation)
    val currentLocationFlow: MutableStateFlow<MutableState<Marker>> get() = _currentLocation

    private var parkingLocation = mutableStateOf(Marker("parking", false, 0.0, 0.0))
    private val _parkingLocation = MutableStateFlow(parkingLocation)
    val parkingLocationFlow: MutableStateFlow<MutableState<Marker>> get() = _parkingLocation

    private var popupIsActive = mutableStateOf(false)
    private val _popupIsActive = MutableStateFlow(popupIsActive)
    val popupIsActiveFlow: MutableStateFlow<MutableState<Boolean>> get() = _popupIsActive


    init {
        LocationManager.onLocationUpdated(this) {
            currentLocation.value = currentLocation.value.copy(
                latitude = it.coordinates.latitude,
                longitude = it.coordinates.longitude
            )
        }
    }

    fun selectCurrentLocation() {
        parkingLocation.value = parkingLocation.value.copy(selected = false)
        currentLocation.value = currentLocation.value.copy(selected = true)
    }

    fun selectParkingLocation() {
        currentLocation.value = currentLocation.value.copy(selected = false)
        parkingLocation.value = parkingLocation.value.copy(selected = true)
    }

    fun deleteParkingLocation() {
        parkingLocation.value = parkingLocation.value.copy(latitude = 0.0, longitude = 0.0)
    }

    fun setParkingLocation(latitude: Double, longitude: Double) {
        parkingLocation.value = Marker("parking", false, latitude, longitude)
    }
}