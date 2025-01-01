package com.horionDev.climbingapp.domain.repositories

import CragDetailsDto
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.Crag

interface CragRepository {
    suspend fun getCragsAroundPosition(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<Crag>, ErrorResponse>

    suspend fun getCragDetails(cragId: Int): ResultWrapper<CragDetailsDto, ErrorResponse>
    suspend fun getAllCrags(): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun fromArea(areaId: String): ResultWrapper<List<Crag>, ErrorResponse>
}