package com.example.camperpro.android.mainmap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.model.*
import com.example.camperpro.domain.usecases.FetchAds
import com.example.camperpro.domain.usecases.FetchSpotAtLocationUseCase
import com.example.camperpro.domain.usecases.SortSpots
import com.example.camperpro.utils.SortOption
import com.jetbrains.kmm.shared.data.ResultWrapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchSpotAtLocationUseCase: FetchSpotAtLocationUseCase,
    private val fetchAds: FetchAds,
    private val sortSpots: SortSpots
) : ViewModel() {

    private val spots = savedStateHandle.getStateFlow("spots", emptyList<Spot>())

    private val spotRepresentation = savedStateHandle.getStateFlow(
        "spotRepresentation",
        SpotPresentation(emptyList<Spot>(), SpotSource.DEFAULT)
    )

    private val ads = savedStateHandle.getStateFlow("ads", emptyList<Ad>())
    private val loading = savedStateHandle.getStateFlow("loading", false)
    private val verticalListIsShowing =
        savedStateHandle.getStateFlow("verticalListIsShowing", false)
    private val cameraIsOutOfRadiusLimit =
        savedStateHandle.getStateFlow("cameraIsOutOfRadiusLimit", false)

    //    flow can only take 5 element max?
    val placeSearched = savedStateHandle.getStateFlow("placeSearched", "")
    val sortedSpots = savedStateHandle.getStateFlow("spotsSorted", emptyList<Spot>())

    val state = combine(
        spotRepresentation, ads, loading, verticalListIsShowing, cameraIsOutOfRadiusLimit
    ) { spotRepresentation, ads, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit ->
        MainMapState(
            spotRepresentation = spotRepresentation,
            ads = ads,
            isLoading,
            verticalListIsShowing,
            cameraIsOutOfRadiusLimit
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), MainMapState(
            spotRepresentation = SpotPresentation(emptyList(), SpotSource.DEFAULT),
            emptyList(),
            isLoading = false,
            verticalListIsShowing = false,
            cameraIsOutOfRadiusLimit = false
        )
    )

    fun swapVerticalList() {
        savedStateHandle["verticalListIsShowing"] = !verticalListIsShowing.value
    }

    fun showSpots(location: Location) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchSpotAtLocationUseCase(location)) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {
                    savedStateHandle["spotRepresentation"] = SpotPresentation(
                        call.value!!,
                        SpotSource.AROUND_ME
                    )
                    savedStateHandle["spotsSorted"] = call.value
                    savedStateHandle["placeSearched"] = ""
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun showSpotsAroundPlace(place: Place) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchSpotAtLocationUseCase(place.location)) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {
                    //                    savedStateHandle["spots"] = call.value
                    savedStateHandle["spotRepresentation"] =
                        SpotPresentation(call.value!!, SpotSource.AROUND_PLACE)
                    savedStateHandle["placeSearched"] = place.name
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun getAds() {
        viewModelScope.launch {
            when (val call = fetchAds()) {
                is ResultWrapper.Failure -> {}
                is ResultWrapper.Success -> savedStateHandle["ads"] = call.value
            }
        }
    }

    fun onSortingOptionSelected(sortOption: SortOption) {
        viewModelScope.launch {
            when (val res = sortSpots(sortOption, spotRepresentation.value.spots)) {
                is ResultWrapper.Failure -> {}
                is ResultWrapper.Success -> savedStateHandle["spotsSorted"] = res.value
            }
        }
    }
}
