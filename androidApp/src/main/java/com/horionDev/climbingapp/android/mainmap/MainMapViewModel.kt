package com.horionDev.climbingapp.android.mainmap

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.composition.Location
import com.horionDev.climbingapp.domain.model.composition.AppMarker
import com.horionDev.climbingapp.domain.model.composition.UpdateSource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.horionDev.climbingapp.data.repositories.Areas
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.repositories.AreaRepository
import com.horionDev.climbingapp.domain.repositories.CragRepository
import com.horionDev.climbingapp.domain.repositories.UserRepository
import com.horionDev.climbingapp.utils.SessionManager.user
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import toMarker

class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val crags: CragRepository,
    private val users: UserRepository,
    private val areas: AreaRepository
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

    val loading = savedStateHandle.getStateFlow("loading", false)
    private val verticalListIsShowing =
        savedStateHandle.getStateFlow("verticalListIsShowing", false)
    private val cameraIsOutOfRadiusLimit =
        savedStateHandle.getStateFlow("cameraIsOutOfRadiusLimit", false)
    val updateSource = savedStateHandle.getStateFlow("updateSource", UpdateSource.DEFAULT)
    val placeSearched = savedStateHandle.getStateFlow("placeSearched", "")
    val cragsMarker = savedStateHandle.getStateFlow("crags", emptyList<Crag>())
    val dealersSorted = savedStateHandle.getStateFlow("dealersSorted", emptyList<Crag>())
    val areasFetched = savedStateHandle.getStateFlow("areas", emptyList<Area>())

    private val _event = MutableSharedFlow<MainMapEvent>()
    val event = _event.asSharedFlow()

//    init {
//        savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
//        markers.clear()
//        markers.addAll(climbingRoutesFrance.toMarker().toList())
//        savedStateHandle["crags"] = climbingRoutesFrance.toList()
//        savedStateHandle["dealersSorted"] = climbingRoutesFrance.toList()
//    }

    val state = combine(
        updateSource, loading, verticalListIsShowing, cameraIsOutOfRadiusLimit
    ) { updateSource, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit ->
        MainMapState(
            updateSource = updateSource,
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

    fun showAreas() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val result = areas.getAllAreas()) {
                is ResultWrapper.Success -> {
                    val areasFetched = result.value
                    savedStateHandle["areas"] = areasFetched.toList().filter { it.name == "Arco" }
                    savedStateHandle["loading"] = false
                }

                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun showCrags(location: Location, seeAllMarkers: Boolean = false) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
//            when (val call = crags.getCragsAroundPosition(location.latitude, location.longitude)) {
            when (val call = crags.getAllCrags()) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }

                is ResultWrapper.Success -> {
                    Log.d("BOTTOM", "showSpots: ")
                    val spots = call.value
                    savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
                    markers.clear()
                    markers.addAll(spots!!.toMarker().toList())//.subList(0, 5000)
                    savedStateHandle["dealers"] = spots.toList()
                    savedStateHandle["dealersSorted"] = spots.toList()
                    savedStateHandle["crags"] = spots.toList()
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

    fun showBookmarks() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val result = users.fetchFavorite(user.id)) {
                is ResultWrapper.Success -> {
                    val spots = result.value
                    savedStateHandle["updateSource"] = UpdateSource.AROUND_ME
                    markers.clear()
                    markers.addAll(spots.toMarker().toList())
                    savedStateHandle["dealers"] = spots.toList()
                    savedStateHandle["dealersSorted"] = spots.toList()
                    savedStateHandle["crags"] = spots.toList()
                    savedStateHandle["placeSearched"] = ""
                    updateMapRegion(markers)

                    savedStateHandle["loading"] = false

                }

                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun showCragsFromArea(areaId: String) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val result = crags.fromArea(areaId)) {
                is ResultWrapper.Success -> {
                    Log.d("BOTTOM", "showSpots: ")
                    val spots = result.value
                    savedStateHandle["updateSource"] = UpdateSource.AREA
                    markers.clear()
                    markers.addAll(spots.toMarker().toList())
                    savedStateHandle["dealers"] = spots.toList()
                    savedStateHandle["dealersSorted"] = spots.toList()
                    savedStateHandle["crags"] = spots.toList()
                    savedStateHandle["placeSearched"] = ""
                    updateMapRegion(markers)
                    savedStateHandle["loading"] = false
                }

                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    sealed class MainMapEvent {
        data class UpdateRegion(val bounds: LatLngBounds, val padding: Int) : MainMapEvent()
    }
}
