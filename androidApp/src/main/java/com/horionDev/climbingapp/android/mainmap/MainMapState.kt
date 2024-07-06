package com.horionDev.climbingapp.android.mainmap

import com.horionDev.climbingapp.domain.model.Ad
import com.horionDev.climbingapp.domain.model.composition.UpdateSource

class MainMapState(
    val updateSource: UpdateSource = UpdateSource.DEFAULT,
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val verticalListIsShowing: Boolean = false,
    val cameraIsOutOfRadiusLimit: Boolean = false,
    val placeSearched: String = ""
)