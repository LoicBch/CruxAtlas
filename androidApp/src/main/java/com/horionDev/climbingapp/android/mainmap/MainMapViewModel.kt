package com.horionDev.climbingapp.android.mainmap

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.data.model.dto.LaundryDto
import com.horionDev.climbingapp.domain.model.Ad
import com.horionDev.climbingapp.domain.model.Dealer
import com.horionDev.climbingapp.domain.model.Event
import com.horionDev.climbingapp.domain.model.Place
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.domain.model.composition.AppMarker
import com.horionDev.climbingapp.domain.model.composition.UpdateSource
import com.horionDev.climbingapp.utils.SortOption
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.ceuse
import com.horionDev.climbingapp.domain.repositories.CragRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import toMarker

class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val crags: CragRepository
) : ViewModel() {

    private var previousSelectedMarkerIndex = 0

    private var markers = mutableStateListOf<AppMarker>()
    private val _markers = MutableStateFlow(markers)
    val markersFlow: StateFlow<List<AppMarker>> get() = _markers

    fun selectMarker(index: Int) {
        markers[previousSelectedMarkerIndex] =
            markers[previousSelectedMarkerIndex].copy(selected = false)
        markers[index] = markers[index].copy(selected = true)
        previousSelectedMarkerIndex = index
    }

    private val ads = savedStateHandle.getStateFlow("ads", emptyList<Ad>())
    val loading = savedStateHandle.getStateFlow("loading", false)
    private val verticalListIsShowing =
        savedStateHandle.getStateFlow("verticalListIsShowing", false)
    private val cameraIsOutOfRadiusLimit =
        savedStateHandle.getStateFlow("cameraIsOutOfRadiusLimit", false)

    val updateSource = savedStateHandle.getStateFlow("updateSource", UpdateSource.DEFAULT)

    val placeSearched = savedStateHandle.getStateFlow("placeSearched", "")

    val events = savedStateHandle.getStateFlow("events", emptyList<Event>())
    val dealers = savedStateHandle.getStateFlow("dealers", emptyList<Dealer>())
    val laundry = savedStateHandle.getStateFlow("laundry", emptyList<Crag>())

    val dealersSorted = savedStateHandle.getStateFlow("dealersSorted", emptyList<Crag>())

    private val _event = MutableSharedFlow<MainMapEvent>()
    val event = _event.asSharedFlow()

//    private val climbingRoutesFrance = listOf(
//        ceuse, ceuse
//    )

//    init {
//        savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
//        markers.clear()
//        markers.addAll(climbingRoutesFrance.toMarker().toList())
//        savedStateHandle["laundry"] = climbingRoutesFrance.toList()
//        savedStateHandle["dealersSorted"] = climbingRoutesFrance.toList()
//    }

    val state = combine(
        updateSource, ads, loading, verticalListIsShowing, cameraIsOutOfRadiusLimit
    ) { updateSource, ads, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit ->
        MainMapState(
            updateSource = updateSource,
            ads = ads,
            isLoading,
            verticalListIsShowing,
            cameraIsOutOfRadiusLimit
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), MainMapState(
            updateSource = UpdateSource.DEFAULT,
            isLoading = false,
            verticalListIsShowing = false,
            cameraIsOutOfRadiusLimit = false
        )
    )

    fun swapVerticalList() {
        savedStateHandle["verticalListIsShowing"] = !verticalListIsShowing.value
    }

    private fun updateMapRegion(markers: List<AppMarker>) {
        var minLat = 90.0
        var maxLat = -90.0
        var minLon = 180.0
        var maxLon = -180.0

        markers.forEach {
            minLat = minOf(minLat, it.latitude)
            maxLat = maxOf(maxLat, it.latitude)
            minLon = minOf(minLon, it.longitude)
            maxLon = maxOf(maxLon, it.longitude)
        }

        val bounds = LatLngBounds.builder()
            .include(LatLng(maxLat, maxLon))
            .include(LatLng(minLat, minLon))
            .build()

        viewModelScope.launch {
            _event.emit(MainMapEvent.UpdateRegion(bounds, 15))
        }
    }


    fun showCrags(location: Location, seeAllMarkers: Boolean = false) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = crags.getCragsAroundPosition(location.latitude, location.longitude)) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }

                is ResultWrapper.Success -> {
                    Log.d("BOTTOM", "showSpots: ")
                    val spots = call.value
                    savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
                    markers.clear()
                    markers.addAll(spots!!.toMarker().toList())
                    savedStateHandle["dealers"] = spots.toList()
                    savedStateHandle["dealersSorted"] = spots.toList()
                    savedStateHandle["laundry"] = spots.toList()
                    savedStateHandle["placeSearched"] = ""
                    if (seeAllMarkers) {
                        updateMapRegion(markers)
                    }
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun onCrossLocationClicked() {
        savedStateHandle["placeSearched"] = ""
    }

    sealed class MainMapEvent {
        data class UpdateRegion(val bounds: LatLngBounds, val padding: Int) : MainMapEvent()
    }
}
