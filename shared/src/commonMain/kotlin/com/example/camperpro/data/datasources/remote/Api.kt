package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.model.responses.SpotResponse
import com.example.camperpro.data.model.responses.StarterResponse
import com.example.camperpro.data.repositories.Spots
import com.example.camperpro.domain.model.*
import com.jetbrains.kmm.shared.data.ResultWrapper

interface Api {
    suspend fun starter(): ResultWrapper<Starter>
    suspend fun getSpotAtLocation(location: Location): ResultWrapper<List<Spot>>
    suspend fun getAds(): ResultWrapper<List<Ad>>
    suspend fun getPartners(): ResultWrapper<List<Partner>>
    suspend fun getLocationSuggestions(input: String): ResultWrapper<List<Place>>
}