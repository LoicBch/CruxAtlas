package com.example.camperpro.android.myLocation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.LocationInfos
import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.domain.usecases.GetLocationInfos
import com.example.camperpro.managers.location.LocationManager
import com.example.camperpro.utils.KMMPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyLocationViewModel(
    private val getLocationInfos: GetLocationInfos, private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var activeLocation = savedStateHandle.getStateFlow("active_location", LocationInfos())

    private var currentLocation = mutableStateOf(Marker("current", false, 0.0, 0.0))
    private val _currentLocation = MutableStateFlow(currentLocation)
    val currentLocationFlow: MutableStateFlow<MutableState<Marker>> get() = _currentLocation

    private var parkingLocation = mutableStateOf(Marker("parking", false, 0.0, 0.0))
    private val _parkingLocation = MutableStateFlow(parkingLocation)
    val parkingLocationFlow: MutableStateFlow<MutableState<Marker>> get() = _parkingLocation

    private var popupIsActive = mutableStateOf(false)
    private val _popupIsActive = MutableStateFlow(popupIsActive)
    val popupIsActiveFlow: MutableStateFlow<MutableState<Boolean>> get() = _popupIsActive

    fun init(context: Application) {
        LocationManager.onLocationUpdated(this) {
            currentLocation.value = currentLocation.value.copy(
                latitude = it.coordinates.latitude, longitude = it.coordinates.longitude
            )
        }

        val parkingLocationSaved = KMMPreference(context = context).getString("parking_location")
        if (parkingLocationSaved != null) {
            val latLng = parkingLocationSaved.split(",")
            val latitude = latLng[0]
            val longitude = latLng[1]
            parkingLocation.value = parkingLocation.value.copy(
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble()
            )
        }

    }

    fun selectCurrentLocation() {

        val currentLoc = Location(currentLocation.value.latitude, currentLocation.value.longitude)

        Log.d("LOCATIONMAP", "${currentLoc.latitude} ${currentLoc.longitude}")

        viewModelScope.launch {
            when (val res = getLocationInfos(currentLoc)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["active_location"] = res.value
                }
                is ResultWrapper.Failure -> {
                }
            }
        }

        parkingLocation.value = parkingLocation.value.copy(selected = false)
        currentLocation.value = currentLocation.value.copy(selected = true)
    }

    fun selectParkingLocation(location: Location) {

        viewModelScope.launch {
            when (val res = getLocationInfos(location)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["active_location"] = res.value
                    Log.d("MAP", "selectParkingLocation: ")
                }
                is ResultWrapper.Failure -> {
                    Log.d("MAP", res.throwable.toString())
                }
            }
        }

        currentLocation.value = currentLocation.value.copy(selected = false)
        parkingLocation.value = parkingLocation.value.copy(selected = true)
    }

    fun deleteParkingLocation(context: Application) {
        KMMPreference(context = context).remove("parking_location")
        parkingLocation.value = parkingLocation.value.copy(latitude = 0.0, longitude = 0.0)
    }

    fun saveParkingLocation(context: Application, latitude: Double, longitude: Double) {
        KMMPreference(context = context).put("parking_location", "$latitude,$longitude")
        selectParkingLocation(Location(latitude, longitude))
    }
}