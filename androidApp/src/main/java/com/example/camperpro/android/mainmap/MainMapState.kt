package com.example.camperproglobal.android.mainmap

import com.example.camperpro.domain.model.Spot

data class MainMapState(
    val spots: List<Spot> = emptyList(),
    val loading : Boolean = false
)