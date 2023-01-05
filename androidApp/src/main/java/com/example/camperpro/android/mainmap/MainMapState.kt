package com.example.camperpro.android.mainmap

import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.model.SpotPresentation
import com.example.camperpro.domain.model.SpotSource

data class MainMapState(
//    val spots: List<Spot> = emptyList(),
    val spotRepresentation : SpotPresentation = SpotPresentation(emptyList(), SpotSource.DEFAULT),
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val verticalListIsShowing: Boolean = false,
    val cameraIsOutOfRadiusLimit: Boolean = false,
    val placeSearched: String = ""
)