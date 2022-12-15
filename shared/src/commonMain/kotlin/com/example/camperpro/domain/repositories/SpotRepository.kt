package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.Spot
import com.jetbrains.kmm.shared.data.ResultWrapper

interface SpotRepository {
    suspend fun all(): ResultWrapper<List<Spot>>
//    suspend fun atLocation(location: Location): ResultWrapper<List<Spot>>
}