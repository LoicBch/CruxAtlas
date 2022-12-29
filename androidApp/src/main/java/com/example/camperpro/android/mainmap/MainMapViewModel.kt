package com.example.camperpro.android.mainmap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.Location
import com.example.camperpro.domain.model.Place
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.usecases.FetchAds
import com.example.camperpro.domain.usecases.FetchSpotAtLocationUseCase
import com.jetbrains.kmm.shared.data.ResultWrapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchSpotAtLocationUseCase: FetchSpotAtLocationUseCase,
    private val fetchAds: FetchAds
) : ViewModel() {

    private val spots = savedStateHandle.getStateFlow("spots", emptyList<Spot>())
    private val ads = savedStateHandle.getStateFlow("ads", emptyList<Ad>())
    private val loading = savedStateHandle.getStateFlow("loading", false)
    private val verticalListIsShowing =
        savedStateHandle.getStateFlow("verticalListIsShowing", false)
    private val cameraIsOutOfRadiusLimit =
        savedStateHandle.getStateFlow("cameraIsOutOfRadiusLimit", false)

    //    flow can only take 5 element max?
    val placeSearched = savedStateHandle.getStateFlow("placeSearched", "")

    val state = combine(
        spots, ads, loading, verticalListIsShowing, cameraIsOutOfRadiusLimit
    ) { spots, ads, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit ->
        MainMapState(
            spots = spots, ads = ads, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), MainMapState(
            emptyList(),
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
                    savedStateHandle["spots"] = call.value
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
                    savedStateHandle["spots"] = call.value
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

}
