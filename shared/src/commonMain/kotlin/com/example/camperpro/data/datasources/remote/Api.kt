package com.example.camperpro.data.datasources.remote

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.*
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.model.composition.LocationInfos

interface Api {
    suspend fun starter(): ResultWrapper<Starter>
    suspend fun getSpotAtLocation(
        location: Location,
        brandFilters: List<Int>?,
        serviceFilters: List<Int>?
    ): ResultWrapper<List<Dealer>>

    suspend fun getAds(): ResultWrapper<List<Ad>>
    suspend fun getLocationSuggestions(
        input: String
    ): ResultWrapper<List<Place>>

    suspend fun getChecklists(): ResultWrapper<List<CheckList>>
    suspend fun getPartners(): ResultWrapper<List<Partner>>
    suspend fun getEvents(countriesFilters: String): ResultWrapper<List<Event>>
    suspend fun locate(lat: String, long: String): ResultWrapper<LocationInfos>

}