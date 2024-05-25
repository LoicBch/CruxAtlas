package com.appmobiledition.laundryfinder.data.datasources.remote

import com.appmobiledition.laundryfinder.data.model.ErrorMessage
import com.appmobiledition.laundryfinder.data.ResultWrapper
import com.appmobiledition.laundryfinder.data.model.dto.LaundryDto
import com.appmobiledition.laundryfinder.domain.model.Place

interface Api {
    suspend fun getLaudry(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<LaundryDto>, ErrorMessage>

    suspend fun getLocationSuggestions(
        input: String
    ): ResultWrapper<List<Place>, ErrorMessage>
}