package com.horionDev.climbingapp.data.datasources.remote

import com.horionDev.climbingapp.data.model.ErrorMessage
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.LaundryDto
import com.horionDev.climbingapp.domain.model.Place
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.model.composition.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.composition.Report
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.User

interface Api {

    //Auth
    //    suspend fun subscribeUser(userId: Int, productType: SubType): ResultWrapper<List<String>, ErrorResponse>
    suspend fun login(authRequest: AuthRequest): ResultWrapper<AuthResponse, ErrorResponse>
    suspend fun signup(
        username: String,
        password: String,
        email: String
    ): ResultWrapper<AuthResponse, ErrorResponse>

    suspend fun authenticate(token: String): ResultWrapper<User, ErrorResponse>

    //Crag
    suspend fun getCragDetails(cragId: Int): ResultWrapper<Crag, ErrorResponse>
    suspend fun getCragAroundLocation(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<Crag>, ErrorResponse>

    suspend fun addSpotAsFavoriteToUser(
        userId: Int,
        spotId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun removeSpotAsFavoriteForUser(
        userId: Int,
        spotId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun getSpotsFavoriteByUser(userId: Int): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun addSpot(crag: Crag): ResultWrapper<Crag, ErrorResponse>
    suspend fun get3DModel(cragId: Int): ResultWrapper<String, ErrorResponse>
    suspend fun getLocationSuggestions(
        input: String
    ): ResultWrapper<List<Place>, ErrorMessage>
}