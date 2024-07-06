package com.horionDev.climbingapp.data.datasources.remote

import com.horionDev.climbingapp.data.model.ErrorMessage
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.LaundryDto
import com.horionDev.climbingapp.domain.model.Place

interface Api {
    suspend fun getLaudry(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<LaundryDto>, ErrorMessage>

    suspend fun getLocationSuggestions(
        input: String
    ): ResultWrapper<List<Place>, ErrorMessage>
}