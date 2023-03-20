package com.example.camperpro.android.mainmap

import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.composition.UpdateSource

class MainMapState(
    val updateSource: UpdateSource = UpdateSource.DEFAULT,
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val verticalListIsShowing: Boolean = false,
    val cameraIsOutOfRadiusLimit: Boolean = false,
    val placeSearched: String = ""
)