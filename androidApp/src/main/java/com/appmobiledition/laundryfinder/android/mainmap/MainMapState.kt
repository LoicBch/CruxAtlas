package com.appmobiledition.laundryfinder.android.mainmap

import com.appmobiledition.laundryfinder.domain.model.Ad
import com.appmobiledition.laundryfinder.domain.model.composition.UpdateSource

class MainMapState(
    val updateSource: UpdateSource = UpdateSource.DEFAULT,
    val ads: List<Ad> = emptyList(),
    val isLoading: Boolean = false,
    val verticalListIsShowing: Boolean = false,
    val cameraIsOutOfRadiusLimit: Boolean = false,
    val placeSearched: String = ""
)