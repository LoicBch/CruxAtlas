package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.data.datasources.remote.CruxAtlasApi
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.repositories.CragRepository

class Crags(private var cruxAtlasApi: Api): CragRepository {
    override suspend fun getCragsAroundPosition(latitude: Double, longitude: Double) = cruxAtlasApi.getCragAroundLocation(latitude, longitude)
}