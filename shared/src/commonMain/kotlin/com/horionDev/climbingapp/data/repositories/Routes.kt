package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.domain.repositories.RouteRepository

class Routes(private val cruxAtlasApi: Api) : RouteRepository {
    override suspend fun logRoute(userId: Int, cragId: Int, log: String) =
        cruxAtlasApi.logRoute(userId, cragId, log)
}