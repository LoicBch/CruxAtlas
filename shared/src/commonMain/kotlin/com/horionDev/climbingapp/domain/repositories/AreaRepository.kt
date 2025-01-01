package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.Area

interface AreaRepository {
    suspend fun getAllAreas(): ResultWrapper<List<Area>, ErrorResponse>
}