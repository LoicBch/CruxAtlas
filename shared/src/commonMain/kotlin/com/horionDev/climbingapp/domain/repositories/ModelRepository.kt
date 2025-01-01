package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.responses.NothingResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse

interface ModelRepository {
    suspend fun get(id: String, onPercent: (Double) -> Unit): ResultWrapper<NothingResponse, ErrorResponse>
}