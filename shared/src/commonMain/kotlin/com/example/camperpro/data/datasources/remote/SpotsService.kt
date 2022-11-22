package com.example.camperpro.data.datasources.remote

import com.jetbrains.kmm.shared.data.ResultWrapper
import com.example.camperpro.domain.model.Spot
import com.jetbrains.kmm.shared.domain.model.User
import com.jetbrains.kmm.shared.domain.model.Location

interface SpotsService {
    suspend fun getSpotsAround(location: Location): ResultWrapper<List<Spot>>
}