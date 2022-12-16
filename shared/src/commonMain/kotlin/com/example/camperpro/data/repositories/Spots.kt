package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.Location
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.repositories.SpotRepository
import com.jetbrains.kmm.shared.data.ResultWrapper

class Spots(private var camperProApi: Api) : SpotRepository {
    override suspend fun atLocation(location: Location): ResultWrapper<List<Spot>> = camperProApi.getSpotAtLocation(location)
}
