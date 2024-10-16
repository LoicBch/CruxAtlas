package com.horionDev.climbingapp.data.datasources.remote

import com.horionDev.climbingapp.data.model.ErrorMessage
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.model.responses.NothingResponse
import com.horionDev.climbingapp.domain.model.CragDetails
import com.horionDev.climbingapp.domain.model.NewsItem
import com.horionDev.climbingapp.domain.model.Place
import com.horionDev.climbingapp.domain.model.UserProfile
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.model.composition.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.BoulderLog
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.RouteLog
import com.horionDev.climbingapp.domain.model.entities.User

interface Api {

    //Auth
    //    suspend fun subscribeUser(userId: Int, productType: SubType): ResultWrapper<List<String>, ErrorResponse>
    suspend fun login(authRequest: AuthRequest): ResultWrapper<AuthResponse, ErrorResponse>
    suspend fun signup(
        username: String,
        password: String,
        email: String
    ): ResultWrapper<String, ErrorResponse>

    suspend fun forgotPassword(email: String): ResultWrapper<NothingResponse, ErrorResponse>
    suspend fun authenticate(token: String): ResultWrapper<User, ErrorResponse>

    //Crag
    suspend fun getCragDetails(cragId: Int): ResultWrapper<CragDetails, ErrorResponse>
    suspend fun getCragAroundLocation(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<Crag>, ErrorResponse>

    suspend fun getNews(
        page: Int
    ): ResultWrapper<List<NewsItem>, ErrorResponse>

    suspend fun getPublicProfile(userId: Int): ResultWrapper<UserProfile, ErrorResponse>

    suspend fun getSpotsFavoriteByUser(userId: Int): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun addSpot(crag: Crag): ResultWrapper<Crag, ErrorResponse>
    suspend fun get3DModel(cragId: Int): ResultWrapper<String, ErrorResponse>
    suspend fun getLocationSuggestions(
        input: String
    ): ResultWrapper<List<Place>, ErrorMessage>

    suspend fun logRoute(
        userId: Int,
        cragId: Int,
        log: String
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun fetchFavorite(userId: Int): ResultWrapper<List<String>, ErrorResponse>
    suspend fun fetchBoulderLogs(userId: Int): ResultWrapper<List<Pair<BoulderLog, Boulder>>, ErrorResponse>
    suspend fun fetchRouteLogs(userId: Int): ResultWrapper<List<Pair<RouteLog, Route>>, ErrorResponse>
    suspend fun updatePhoto(userId: Int, byteArray: ByteArray): ResultWrapper<NothingResponse, ErrorResponse>
    suspend fun addRouteLog(
        userId: Int,
        routeId: Int,
        routeLog: RouteLog
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun addBoulderLog(
        userId: Int,
        boulderId: Int,
        boulderLog: BoulderLog
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun updateUser(userDto: UserDto): ResultWrapper<User, ErrorResponse>
    suspend fun addCragAsFavoriteToUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun removeCragAsFavoriteForUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>
}