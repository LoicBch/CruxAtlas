package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.domain.repositories.AreaRepository

class Areas(private val cruxAtlasApi: Api) : AreaRepository {
    override suspend fun getAllAreas() = cruxAtlasApi.getAllAreas()
}