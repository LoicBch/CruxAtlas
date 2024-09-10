package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.responses.NothingResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse

interface RouteRepository {
    suspend fun logRoute(userId: Int, cragId: Int, log: String): ResultWrapper<NothingResponse, ErrorResponse>
}