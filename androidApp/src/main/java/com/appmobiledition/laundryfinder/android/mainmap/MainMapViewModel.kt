package com.appmobiledition.laundryfinder.android.mainmap

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appmobiledition.laundryfinder.data.ResultWrapper
import com.appmobiledition.laundryfinder.data.datasources.remote.Api
import com.appmobiledition.laundryfinder.data.model.dto.LaundryDto
import com.appmobiledition.laundryfinder.domain.model.Ad
import com.appmobiledition.laundryfinder.domain.model.Dealer
import com.appmobiledition.laundryfinder.domain.model.Event
import com.appmobiledition.laundryfinder.domain.model.Place
import com.appmobiledition.laundryfinder.domain.model.composition.Location
import com.appmobiledition.laundryfinder.domain.model.composition.Marker
import com.appmobiledition.laundryfinder.domain.model.composition.UpdateSource
import com.appmobiledition.laundryfinder.utils.SortOption
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import toMarker

// pour reduir la taille des bundles on peut garder les lieux query dans une list et utiliser des list d'id pour les autre list dont on a besoin comme la vertical list ou les autres

class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    //    private val fetchDealersAtLocationUseCase: FetchDealersAtLocationUseCase,
    //    private val fetchEvents: FetchEvents,
    //    private val fetchAds: FetchAds,
    //    private val sortDealer: SortDealer,
    //    private val sortEvents: SortEvents
    private val api: Api
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
    val laundry = savedStateHandle.getStateFlow("laundry", emptyList<LaundryDto>())

    val eventsSorted = savedStateHandle.getStateFlow("eventsSorted", emptyList<Event>())
    val dealersSorted = savedStateHandle.getStateFlow("dealersSorted", emptyList<LaundryDto>())

    private val _event = MutableSharedFlow<MainMapEvent>()
    val event = _event.asSharedFlow()

    init {
        showLaundry(Location(0.0, 0.0))
    }

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

    fun showLaundry(location: Location) {
        viewModelScope.launch {
            savedStateHandle["loading"] = true
            when (val laundries = api.getLaudry(location.latitude, location.longitude)) {
                is ResultWrapper.Failure.HttpError -> {}
                ResultWrapper.Failure.NetworkError -> {
                }

                is ResultWrapper.Failure -> {

                }

                is ResultWrapper.Success -> {
                    savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
                    markers.clear()
                    markers.addAll(laundries.value!!.toMarker().toList())
                    savedStateHandle["laundry"] = laundries.value!!.toList()
                    savedStateHandle["dealersSorted"] = laundries.value!!.toList()
                    savedStateHandle["placeSearched"] = ""
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun showSpots(location: Location, seeAllMarkers: Boolean = false) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            //            when (val call = fetchDealersAtLocationUseCase(location)) {
            //                is ResultWrapper.Failure -> {
            //                    savedStateHandle["loading"] = false
            //                }
            //                is ResultWrapper.Success -> {
            //                    Log.d("BOTTOM", "showSpots: ")
            //                    val spots = call.value
            //                    savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
            //                    markers.clear()
            //                    markers.addAll(spots!!.toMarker().toList())
            //                    savedStateHandle["dealers"] = spots.toList()
            //                    savedStateHandle["dealersSorted"] = spots.toList()
            //                    savedStateHandle["placeSearched"] = ""
            //                    if (seeAllMarkers) {
            //                        updateMapRegion(markers)
            //                    }
            //                    savedStateHandle["loading"] = false
            //                }
            //        }
        }
    }

    fun showEvents(seeAllMarkers: Boolean = false) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            //            when (val call = fetchEvents()) {
            //                is ResultWrapper.Failure -> {
            //                    savedStateHandle["loading"] = false
            //                }
            //                is ResultWrapper.Success -> {
            //
            //                    savedStateHandle["updateSource"] = UpdateSource.EVENTS
            //                    markers.clear()
            //                    markers.addAll(call.value!!.toMarker().toList())
            //                    savedStateHandle["events"] = call.value
            //                    savedStateHandle["eventsSorted"] = call.value
            //                    savedStateHandle["loading"] = false
            //                }
            //            }
        }
    }

    fun showSpotsAroundPlace(place: Place) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val laundries =
                api.getLaudry(place.location.latitude, place.location.longitude)) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }

                is ResultWrapper.Success -> {
                    savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
                    markers.clear()
                    markers.addAll(laundries.value!!.toMarker().toList())
                    savedStateHandle["laundry"] = laundries.value!!.toList()
                    savedStateHandle["dealersSorted"] = laundries.value!!.toList()
                    savedStateHandle["placeSearched"] = ""
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun getAds() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            //            when (val call = fetchAds()) {
            //                is ResultWrapper.Failure -> {
            //                    savedStateHandle["loading"] = false
            //                }
            //                is ResultWrapper.Success -> {
            //                    savedStateHandle["loading"] = false
            //                    savedStateHandle["ads"] = call.value
            //                }
            //            }
        }
    }

    fun onSortingOptionSelected(sortOption: SortOption) {

        viewModelScope.launch {
            if (updateSource.value == UpdateSource.EVENTS) {
                //                when (val res = sortEvents(sortOption, events.value)) {
                //                    is ResultWrapper.Failure -> {
                //                        savedStateHandle["loading"] = false
                //                    }
                //                    is ResultWrapper.Success -> {
                //                        savedStateHandle["eventsSorted"] = res.value
                //                        savedStateHandle["loading"] = false
                //                    }
                //                }
            } else {
                //                when (val res = sortDealer(sortOption, dealers.value)) {
                //                    is ResultWrapper.Failure -> {
                //                        savedStateHandle["loading"] = false
                //                    }
                //                    is ResultWrapper.Success -> {
                //                        savedStateHandle["dealersSorted"] = res.value
                //                        savedStateHandle["loading"] = false
                //                    }
                //                }
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
