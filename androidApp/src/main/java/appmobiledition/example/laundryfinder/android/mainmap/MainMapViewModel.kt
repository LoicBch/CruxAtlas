package com.example.camperpro.android.mainmap

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.Dealer
import com.example.camperpro.domain.model.Event
import com.example.camperpro.domain.model.Place
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.domain.model.composition.UpdateSource
import com.example.camperpro.domain.usecases.*
import com.example.camperpro.utils.SortOption
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import toMarker

// pour reduir la taille des bundles on peut garder les lieux query dans une list et utiliser des list d'id pour les autre list dont on a besoin comme la vertical list ou les autres

class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchDealersAtLocationUseCase: FetchDealersAtLocationUseCase,
    private val fetchEvents: FetchEvents,
    private val fetchAds: FetchAds,
    private val sortDealer: SortDealer,
    private val sortEvents: SortEvents
) : ViewModel() {

    private var previousSelectedMarkerIndex = 0

    private var markers = mutableStateListOf<Marker>()
    private val _markers = MutableStateFlow(markers)
    val markersFlow: StateFlow<List<Marker>> get() = _markers

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

    val eventsSorted = savedStateHandle.getStateFlow("eventsSorted", emptyList<Event>())
    val dealersSorted = savedStateHandle.getStateFlow("dealersSorted", emptyList<Dealer>())

    private val _event = MutableSharedFlow<MainMapEvent>()
    val event = _event.asSharedFlow()

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

    private fun updateMapRegion(markers: List<Marker>) {
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

    fun showSpots(location: Location, seeAllMarkers: Boolean = false) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchDealersAtLocationUseCase(location)) {
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
                    savedStateHandle["placeSearched"] = ""
                    if (seeAllMarkers) {
                        updateMapRegion(markers)
                    }
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun showEvents(seeAllMarkers: Boolean = false) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchEvents()) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {

                    savedStateHandle["updateSource"] = UpdateSource.EVENTS
                    markers.clear()
                    markers.addAll(call.value!!.toMarker().toList())
                    savedStateHandle["events"] = call.value
                    savedStateHandle["eventsSorted"] = call.value
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun showSpotsAroundPlace(place: Place) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchDealersAtLocationUseCase(place.location)) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {
                    markers.clear()
                    markers.addAll(call.value!!.toMarker())

                    savedStateHandle["dealers"] = call.value!!.toList()
                    savedStateHandle["dealersSorted"] = call.value!!.toList()
                    savedStateHandle["updateSource"] = UpdateSource.AROUND_PLACE
                    savedStateHandle["placeSearched"] = place.name
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun getAds() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchAds()) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {
                    savedStateHandle["loading"] = false
                    savedStateHandle["ads"] = call.value
                }
            }
        }
    }

    fun onSortingOptionSelected(sortOption: SortOption) {

        viewModelScope.launch {
            if (updateSource.value == UpdateSource.EVENTS) {
                when (val res = sortEvents(sortOption, events.value)) {
                    is ResultWrapper.Failure -> {
                        savedStateHandle["loading"] = false
                    }
                    is ResultWrapper.Success -> {
                        savedStateHandle["eventsSorted"] = res.value
                        savedStateHandle["loading"] = false
                    }
                }
            } else {
                when (val res = sortDealer(sortOption, dealers.value)) {
                    is ResultWrapper.Failure -> {
                        savedStateHandle["loading"] = false
                    }
                    is ResultWrapper.Success -> {
                        savedStateHandle["dealersSorted"] = res.value
                        savedStateHandle["loading"] = false
                    }
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
