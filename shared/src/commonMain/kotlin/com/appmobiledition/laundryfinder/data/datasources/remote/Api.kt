package com.appmobiledition.laundryfinder.data.datasources.remote

import com.appmobiledition.laundryfinder.data.ResultWrapper
import com.appmobiledition.laundryfinder.domain.model.*
import com.appmobiledition.laundryfinder.domain.model.composition.Location
import com.appmobiledition.laundryfinder.domain.model.composition.LocationInfos

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
    suspend fun getEvents(countriesFilters: String?): ResultWrapper<List<Event>>
    suspend fun locate(lat: String, long: String): ResultWrapper<LocationInfos>

}