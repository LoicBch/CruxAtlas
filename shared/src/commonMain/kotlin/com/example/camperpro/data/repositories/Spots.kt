package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.domain.model.Spot
import com.example.camperpro.domain.repositories.SpotRepository
import com.jetbrains.kmm.shared.data.ResultWrapper

class Spots(private var camperProApi: Api) : SpotRepository {

    override suspend fun all(): ResultWrapper<List<Spot>> {

        println(camperProApi.test().spots.toString())

        return camperProApi.getAllSpots()
    }
}

//    override suspend fun atLocation(location: Location): ResultWrapper<List<Spot>> {
//       return camperProApi.getSpotAtLocation(location)
//    }
//}