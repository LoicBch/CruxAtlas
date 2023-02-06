package com.example.camperpro.android.mainmap

import com.example.camperpro.domain.model.Ad
import com.example.camperpro.domain.model.composition.Marker
import com.example.camperpro.domain.model.composition.UpdateSource

class MainMapState(
    val markersUpdate: Pair<UpdateSource, List<Marker<Any>>> = Pair(UpdateSource.DEFAULT, listOf()),
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val verticalListIsShowing: Boolean = false,
    val cameraIsOutOfRadiusLimit: Boolean = false,
    val placeSearched: String = ""
)