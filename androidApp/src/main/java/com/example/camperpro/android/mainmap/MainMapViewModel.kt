package com.example.camperpro.android.mainmap

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camperproglobal.android.mainmap.MainMapState
import com.example.camperpro.domain.usecases.FetchSpotAtLocationUseCase
import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.domain.model.Location
import com.example.camperpro.domain.model.Spot
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

    val state = combine(spots, loading) { spots, loading ->
        MainMapState(
            spots = spots.map { it.copy(name = it.name.lowercase()) },
            loading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainMapState(emptyList(), false)
    )

    fun getSpotAroundPos(location: Location) {
        savedStateHandle["loading"] = true
        viewModelScope.launch {
            when (val call = fetchSpotAtLocationUseCase(location)) {
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
