package com.horionDev.climbingapp.android.areaDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horionDev.climbingapp.android.parcelable.AreaParcel
import com.horionDev.climbingapp.android.parcelable.toParcelable
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.PhotoDto
import com.horionDev.climbingapp.data.repositories.Areas
import com.horionDev.climbingapp.data.repositories.Crags
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.repositories.AreaRepository
import com.horionDev.climbingapp.domain.repositories.CragRepository
import kotlinx.coroutines.launch

class AreaDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val areas: AreaRepository,
    private val crags: CragRepository
) : ViewModel() {

    val photos = savedStateHandle.getStateFlow("photos", emptyList<PhotoDto>())
    val isFavorite = savedStateHandle.getStateFlow("isFavorite", false)
    val area = savedStateHandle.getStateFlow("area", AreaParcel(0, "", "", emptyList()))
    val cragsFromArea = savedStateHandle.getStateFlow("crags", emptyList<Crag>())

    fun init(area: Area) {
        savedStateHandle["area"] = area.toParcelable()
        savedStateHandle["photos"] = listOf(
            PhotoDto(
                "https://media.istockphoto.com/id/1441026821/vector/no-picture-available-placeholder-thumbnail-icon-illustration.jpg?s=612x612&w=0&k=20&c=7K9T9bguFyJyKOTvPkdoTWZYRWA3zGvx_xQI53BT0wg=",
            )
        )
        getCragsFromArea(area.id.toString())
    }

    private fun getCragsFromArea(areaId: String) {
        viewModelScope.launch {
            when (val result = crags.fromArea(areaId)) {
                is ResultWrapper.Success -> {
                    savedStateHandle["crags"] = result.value
                }

                is ResultWrapper.Failure -> {

                }
            }
        }
    }

    fun permuteFavorite(areaId: String) {

    }

}