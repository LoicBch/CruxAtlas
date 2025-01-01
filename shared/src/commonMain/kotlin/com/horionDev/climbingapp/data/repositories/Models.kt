package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.data.datasources.remote.CruxAtlasApi
import com.horionDev.climbingapp.domain.repositories.ModelRepository

class Models(private val api: Api) : ModelRepository {
    override suspend fun get(id: String, onPercent: (Double) -> Unit) = api.getModel(id, onPercent)
}