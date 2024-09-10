package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.CragDetails
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.Crag

interface CragRepository {
    suspend fun getCragsAroundPosition(latitude: Double, longitude: Double): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun getCragDetails(cragId: Int): ResultWrapper<CragDetails, ErrorResponse>
}