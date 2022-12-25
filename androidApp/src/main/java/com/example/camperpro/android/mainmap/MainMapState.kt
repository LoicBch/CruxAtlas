package com.example.camperpro.android.mainmap

import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.Spot

data class MainMapState(
    val spots: List<Spot> = emptyList(),
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val verticalListIsShowing: Boolean = false,
    val cameraIsOutOfRadiusLimit: Boolean = false
)

enum class MapSearchSource {
    AROUND_ME, LOCATION
}