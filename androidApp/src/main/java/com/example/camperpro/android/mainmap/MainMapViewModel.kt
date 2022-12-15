package com.example.camperpro.android.mainmap

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.usecases.FetchSpotAtLocationUseCase
import com.jetbrains.kmm.shared.data.ResultWrapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainMapViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchSpotAtLocationUseCase: FetchSpotAtLocationUseCase
) : ViewModel() {

    private val spots = savedStateHandle.getStateFlow("spots", emptyList<Spot>())
    private val loading = savedStateHandle.getStateFlow("loading", false)
    private val verticalListIsShowing =
        savedStateHandle.getStateFlow("verticalListIsShowing", false)
    private val cameraIsOutOfRadiusLimit =
        savedStateHandle.getStateFlow("cameraIsOutOfRadiusLimit", false)

    val state = combine(
        spots,
        loading,
        verticalListIsShowing,
        cameraIsOutOfRadiusLimit
    ) { spots, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit ->
        MainMapState(
            spots = spots, isLoading, verticalListIsShowing, cameraIsOutOfRadiusLimit
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainMapState(
            emptyList(),
            isLoading = false,
            verticalListIsShowing = false,
            cameraIsOutOfRadiusLimit = false
        )
    )

    fun showVerticalList() {
        savedStateHandle["verticalListIsShowing"] = !verticalListIsShowing.value
    }

    fun getSpots() {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchSpotAtLocationUseCase()) {
                is ResultWrapper.Failure -> {
                    Log.d("TAG", call.throwable.toString())
                    savedStateHandle["loading"] = false
                }
                is ResultWrapper.Success -> {
                    savedStateHandle["spots"] = call.value
                    savedStateHandle["loading"] = false
                }
            }
        }
    }

}
