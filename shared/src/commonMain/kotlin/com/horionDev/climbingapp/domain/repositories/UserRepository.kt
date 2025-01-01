package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.model.responses.NothingResponse
import com.horionDev.climbingapp.domain.model.entities.UserProfile
import com.horionDev.climbingapp.data.model.dto.AuthRequest
import com.horionDev.climbingapp.data.model.dto.BoulderLogDto
import com.horionDev.climbingapp.data.model.dto.RouteLogDto
import com.horionDev.climbingapp.data.model.responses.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.Crag
import com.horionDev.climbingapp.domain.model.entities.Route
import com.horionDev.climbingapp.domain.model.entities.User

interface UserRepository {
    suspend fun login(authRequest: AuthRequest): ResultWrapper<AuthResponse, ErrorResponse>
    suspend fun authenticate(token: String): ResultWrapper<User, ErrorResponse>
    suspend fun signup(
        username: String,
        password: String,
        email: String
    ): ResultWrapper<String, ErrorResponse>

    suspend fun publicProfile(userId: Int): ResultWrapper<UserProfile, ErrorResponse>
    suspend fun forgotPassword(email: String): ResultWrapper<NothingResponse, ErrorResponse>
    suspend fun fetchFavorite(userId: Int): ResultWrapper<List<Crag>, ErrorResponse>
    suspend fun addCragAsFavoriteToUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun removeCragAsFavoriteForUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>
    suspend fun updatePhoto(userId: Int, byteArray: ByteArray): ResultWrapper<NothingResponse, ErrorResponse>
    suspend fun fetchRouteLogs(userId: Int): ResultWrapper<List<Pair<RouteLogDto, Route>>, ErrorResponse>
    suspend fun fetchBoulderLogs(userId: Int): ResultWrapper<List<Pair<BoulderLogDto, Boulder>>, ErrorResponse>
    suspend fun updateUser(userDto: UserDto): ResultWrapper<User, ErrorResponse>
    suspend fun addRouteLog(
        routeLog: RouteLogDto,
        userId: Int,
        routeId: Int
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun addBoulderLog(
        boulderLog: BoulderLogDto,
        userId: Int,
        boulderId: Int
    ): ResultWrapper<NothingResponse, ErrorResponse>
}