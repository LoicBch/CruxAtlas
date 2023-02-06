package com.example.camperpro.data.datasources.remote

import com.example.camperpro.domain.model.*
import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.composition.Location

interface Api {
    suspend fun starter(): ResultWrapper<Starter>
    suspend fun getSpotAtLocation(location: Location): ResultWrapper<List<Dealer>>
    suspend fun getAds(): ResultWrapper<List<Ad>>
    suspend fun getLocationSuggestions(input: String): ResultWrapper<List<Place>>
    suspend fun getPartners(): ResultWrapper<List<Partner>>
    suspend fun getEvents(): ResultWrapper<List<Event>>
}