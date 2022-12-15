package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.model.responses.SpotResponse
import com.example.camperpro.domain.model.Spot
import com.jetbrains.kmm.shared.data.ResultWrapper
import io.ktor.client.statement.*

interface Api {
    suspend fun getAllSpots(): ResultWrapper<List<Spot>>
    suspend fun test(): SpotResponse

//    suspend fun getSpotAtLocation(location: Location): ResultWrapper<SpotResponse>
}