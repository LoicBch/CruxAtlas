package com.horionDev.climbingapp.data.datasources.remote

import CragDetailsDto
import com.horionDev.climbingapp.data.model.ErrorMessage
import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.model.responses.NothingResponse
import com.horionDev.climbingapp.domain.model.entities.NewsItem
import com.horionDev.climbingapp.domain.model.composition.Place
import com.horionDev.climbingapp.domain.model.entities.UserProfile
import com.horionDev.climbingapp.data.model.dto.AuthRequest
import com.horionDev.climbingapp.data.model.dto.BoulderLogDto
import com.horionDev.climbingapp.data.model.dto.RouteLogDto
import com.horionDev.climbingapp.data.model.responses.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.Area
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Model
import com.horionDev.climbingapp.domain.model.entities.Route
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
    suspend fun getCragDetails(cragId: Int): ResultWrapper<CragDetailsDto, ErrorResponse>
    suspend fun getCragAroundLocation(
        latitude: Double,
        longitude: Double
    ): ResultWrapper<List<Crag>, ErrorResponse>
//    suspend fun getParkingSpots(cragId: Int): ResultWrapper<List<ParkingSpotDto>, ErrorResponse>

    suspend fun getNews(
        page: Int
    ): ResultWrapper<List<NewsItem>, ErrorResponse>

    suspend fun getPublicProfile(userId: Int): ResultWrapper<UserProfile, ErrorResponse>

    suspend fun getSpotsFavoriteByUser(userId: Int): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun addSpot(crag: Crag): ResultWrapper<Crag, ErrorResponse>
    suspend fun getModel(modelId: String, onPercent: (Double) -> Unit): ResultWrapper<NothingResponse, ErrorResponse>
    suspend fun getLocationSuggestions(
        input: String
    ): ResultWrapper<List<Place>, ErrorMessage>

    suspend fun logRoute(
        userId: Int,
        cragId: Int,
        log: String
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun fetchFavorite(userId: Int): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun fetchBoulderLogs(userId: Int): ResultWrapper<List<Pair<BoulderLogDto, Boulder>>, ErrorResponse>
    suspend fun fetchRouteLogs(userId: Int): ResultWrapper<List<Pair<RouteLogDto, Route>>, ErrorResponse>
    suspend fun updatePhoto(userId: Int, byteArray: ByteArray): ResultWrapper<NothingResponse, ErrorResponse>
    suspend fun addRouteLog(
        userId: Int,
        routeId: Int,
        routeLog: RouteLogDto
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun addBoulderLog(
        userId: Int,
        boulderId: Int,
        boulderLog: BoulderLogDto
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun updateUser(userDto: UserDto): ResultWrapper<User, ErrorResponse>
    suspend fun addCragAsFavoriteToUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun getAllCrags(): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun getAllAreas(): ResultWrapper<List<Area>, ErrorResponse>

    suspend fun removeCragAsFavoriteForUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun getCragsFromArea(areaId: String): ResultWrapper<List<Crag>, ErrorResponse>
}