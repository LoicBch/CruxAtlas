package com.example.camperpro.data.repositories

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.Dealer
import com.example.camperpro.domain.model.composition.Location
import com.example.camperpro.domain.repositories.DealerRepository

class Dealers(private var camperProApi: Api) : DealerRepository {
    override suspend fun atLocation(
        location: Location,
        brandFilters: List<Int>?,
        serviceFilters: List<Int>?
    ): ResultWrapper<List<Dealer>> =
        camperProApi.getSpotAtLocation(location, brandFilters, serviceFilters)
}
