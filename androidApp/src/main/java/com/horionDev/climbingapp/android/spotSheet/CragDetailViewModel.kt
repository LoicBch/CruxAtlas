package com.horionDev.climbingapp.android.spotSheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.repositories.Crags
import com.horionDev.climbingapp.domain.model.CragDetailsDto
import com.horionDev.climbingapp.domain.model.entities.RouteWithLog
import kotlinx.coroutines.launch

class CragDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val crags: Crags
) : ViewModel() {

    val cragDetails = savedStateHandle.getStateFlow<CragDetailsDto?>("crags", null)

    fun init(cragId: Int){
        viewModelScope.launch {
            when (val result = crags.getCragDetails(cragId)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["crags"] = result.value
                }
                is ResultWrapper.Failure -> {
                    // handle error
                }
            }
        }
    }
}