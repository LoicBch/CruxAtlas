package com.example.camperproglobal.android

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperproglobal.android.mainmap.MainMapState
import com.example.camperproglobal.domain.model.dao.SpotDao
import com.example.camperproglobal.domain.usecases.FetchSpotAtLocation
import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.domain.model.Location
import com.jetbrains.kmm.shared.domain.model.Spot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMapViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val spots = savedStateHandle.getStateFlow("spots", emptyList<Spot>())
    private val connected = savedStateHandle.getStateFlow("connected", false)
    private val userLocation = savedStateHandle.getStateFlow("user_location", 0.0f)

    val state = combine(spots, connected, userLocation) { spots, connected, userLocation ->
        MainMapState(
            spots = spots.map { it.copy(name = it.name.lowercase()) },
            connected,
            userLocation
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainMapState(emptyList(), false, 0.0f)
    )


    fun getSpotAroundPos(location: Location) {
        viewModelScope.launch {
            when (val call = FetchSpotAtLocation(location).execute()) {
                is ResultWrapper.Failure -> Log.d("TAG", call.throwable.toString())
                is ResultWrapper.Success -> savedStateHandle["spots"] = call.value
            }
        }
    }

}