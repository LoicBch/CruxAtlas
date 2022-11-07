package com.example.camperproglobal.android.mainmap

import com.jetbrains.kmm.shared.domain.model.Spot

data class MainMapState(
    val spots: List<Spot> = emptyList(),
    val connected : Boolean,
    val userLocation : Float
)