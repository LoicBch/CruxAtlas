package com.example.camperpro.android.mainmap

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import toMarker

class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchSpotAtLocationUseCase: FetchSpotAtLocationUseCase,
    private val fetchEvents: FetchEvents,
    private val fetchAds: FetchAds,
    private val sortDealer: SortDealer,
    private val sortEvents: SortEvents
) : ViewModel() {

    private val markersUpdate = savedStateHandle.getStateFlow(
        "markersUpdate",
        Pair<UpdateSource, List<Marker<Any>>>(UpdateSource.DEFAULT, emptyList())
    )

    private val ads = savedStateHandle.getStateFlow("ads", emptyList<Ad>())
    private val loading = savedStateHandle.getStateFlow("loading", false)
    private val verticalListIsShowing =
        savedStateHandle.getStateFlow("verticalListIsShowing", false)
    private val cameraIsOutOfRadiusLimit =
        savedStateHandle.getStateFlow("cameraIsOutOfRadiusLimit", false)

    val placeSearched = savedStateHandle.getStateFlow("placeSearched", "")
    val verticalListItems = savedStateHandle.getStateFlow("verticalListItems", emptyList<Any>())

    //    flow can only take 5 element max? :(
    val state = combine(
        markersUpdate, ads, loading, verticalListIsShowing, cameraIsOutOfRadiusLimit
    ) { markersUpdate, ads, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit ->
        MainMapState(
            markersUpdate = markersUpdate,
            ads = ads,
            isLoading,
            verticalListIsShowing,
            cameraIsOutOfRadiusLimit
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), MainMapState(
            markersUpdate = Pair(UpdateSource.DEFAULT, emptyList()),
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
                    savedStateHandle["markersUpdate"] = Pair(
                        UpdateSource.AROUND_ME,
                        call.value!!.toMarker()
                    )
                    savedStateHandle["verticalListItems"] = call.value
                    savedStateHandle["placeSearched"] = ""
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

    fun showEvents() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchEvents()) {
                is ResultWrapper.Failure -> {
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {
                    savedStateHandle["markersUpdate"] = Pair(
                        UpdateSource.EVENTS,
                        call.value!!.toMarker()
                    )
                    savedStateHandle["verticalListItems"] = call.value
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
                    savedStateHandle["markersUpdate"] =
                        Pair(UpdateSource.AROUND_PLACE, call.value!!.toMarker())
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
            // TODO: Faire une fonction d'extension / enum de map pour get le type de value actuellement afficher a partir de la source
            if (markersUpdate.value.first == UpdateSource.EVENTS) {
                when (val res =
                    sortEvents(
                        sortOption,
                        markersUpdate.value.second.map { it.content } as List<Event>)) {
                    is ResultWrapper.Failure -> {}
                    is ResultWrapper.Success -> savedStateHandle["verticalListItems"] = res.value
                }
            } else {

                when (val res =
                    sortDealer(
                        sortOption,
                        markersUpdate.value.second.map { it.content } as List<Dealer>)) {
                    is ResultWrapper.Failure -> {}
                    is ResultWrapper.Success -> savedStateHandle["verticalListItems"] = res.value
                }
            }
        }
    }
}
