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
import com.appmobiledition.laundryfinder.domain.model.Laundry
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

    private val climbingRoutesFrance = listOf(
        LaundryDto(0.toString(), "Climbing spot 0", "Address 0", "Phone 0", "Email 0", "Website 0", "Paris", "75000", "FR", "user_id", "created_at", "Website 0",  48.8566.toString(), 2.3522.toString()),
        LaundryDto(1.toString(), "Climbing spot 1", "Address 1", "Phone 1", "Email 1", "Website 1", "Berlin", "10117", "DE", "user_id", "created_at", "Website 1", 52.5200.toString(), 13.4050.toString()),
        LaundryDto(2.toString(), "Climbing spot 2", "Address 2", "Phone 2", "Email 2", "Website 2", "Stuttgart", "70173", "DE", "user_id", "created_at", "Website 2", 49.4170.toString(), 8.6830.toString()),
        LaundryDto(3.toString(), "Climbing spot 3", "Address 3", "Phone 3", "Email 3", "Website 3", "Dresden", "01067", "DE", "user_id", "created_at", "Website 3", 51.0500.toString(), 13.7330.toString()),
        LaundryDto(4.toString(), "Climbing spot 4", "Address 4", "Phone 4", "Email 4", "Website 4", "Frankfurt", "60311", "DE", "user_id", "created_at", "Website 4", 50.1100.toString(), 8.6800.toString()),
        LaundryDto(5.toString(), "Climbing spot 5", "Address 5", "Phone 5", "Email 5", "Website 5", "Leipzig", "04103", "DE", "user_id", "created_at", "Website 5", 51.3400.toString(), 12.3700.toString()),
        LaundryDto(6.toString(), "Climbing spot 6", "Address 6", "Phone 6", "Email 6", "Website 6", "Munich", "80331", "DE", "user_id", "created_at", "Website 6", 48.7800.toString(), 9.1900.toString()),
        LaundryDto(7.toString(), "Climbing spot 7", "Address 7", "Phone 7", "Email 7", "Website 7", "Hamburg", "20095", "DE", "user_id", "created_at", "Website 7", 53.5500.toString(), 10.0000.toString()),
        LaundryDto(8.toString(), "Climbing spot 8", "Address 8", "Phone 8", "Email 8", "Website 8", "Cologne", "50667", "DE", "user_id", "created_at", "Website 8", 50.9350.toString(), 6.9600.toString()),
        LaundryDto(9.toString(), "Climbing spot 9", "Address 9", "Phone 9", "Email 9", "Website 9", "Nuremberg", "90402", "DE", "user_id", "created_at", "Website 9", 51.5000.toString(), 7.4670.toString()),
        LaundryDto(10.toString(), "Climbing spot 10", "Address 10", "Phone 10", "Email 10", "Website 10", "Dusseldorf", "40210", "DE", "user_id", "created_at", "Website 10",  48.1370.toString(), 11.5760.toString()),
        LaundryDto(11.toString(), "Climbing spot 11", "Address 11", "Phone 11", "Email 11", "Website 11", "Bremen", "28195", "DE", "user_id", "created_at", "Website 11",  52.2290.toString(), 21.0120.toString()),
        LaundryDto(12.toString(), "Climbing spot 12", "Address 12", "Phone 12", "Email 12", "Website 12", "Hannover", "30159", "DE", "user_id", "created_at", "Website 12", 54.7340.toString(), 9.4420.toString()),
        LaundryDto(13.toString(), "Climbing spot 13", "Address 13", "Phone 13", "Email 13", "Website 13", "Dortmund", "44135", "DE", "user_id", "created_at", "Website 13",  49.8950.toString(), 10.9220.toString()),
        LaundryDto(14.toString(), "Climbing spot 14", "Address 14", "Phone 14", "Email 14", "Website 14", "Essen", "45127", "DE", "user_id", "created_at", "Website 14", 50.7750.toString(), 7.1950.toString()))

    init {
        savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
        markers.clear()
        markers.addAll(climbingRoutesFrance.toMarker().toList())
        savedStateHandle["laundry"] = climbingRoutesFrance.toList()
        savedStateHandle["dealersSorted"] = climbingRoutesFrance.toList()
//        showLaundry(Location(0.0, 0.0))
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
