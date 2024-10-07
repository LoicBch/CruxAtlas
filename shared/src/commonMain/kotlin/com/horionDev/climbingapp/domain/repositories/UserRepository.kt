package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.model.responses.NothingResponse
import com.horionDev.climbingapp.domain.model.UserProfile
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.model.composition.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.BoulderLog
import com.horionDev.climbingapp.domain.model.entities.RouteLog
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
    suspend fun fetchFavorite(userId: Int): ResultWrapper<List<String>, ErrorResponse>
    suspend fun addCragAsFavoriteToUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun removeCragAsFavoriteForUser(
        userId: Int,
        cragId: Int
    ): ResultWrapper<List<String>, ErrorResponse>

    suspend fun fetchRouteLogs(userId: Int): ResultWrapper<List<RouteLog>, ErrorResponse>
    suspend fun fetchBoulderLogs(userId: Int): ResultWrapper<List<BoulderLog>, ErrorResponse>
    suspend fun updateUser(userDto: UserDto): ResultWrapper<User, ErrorResponse>
    suspend fun addRouteLog(
        routeLog: RouteLog,
        userId: Int,
        routeId: Int
    ): ResultWrapper<NothingResponse, ErrorResponse>

    suspend fun addBoulderLog(
        boulderLog: BoulderLog,
        userId: Int,
        boulderId: Int
    ): ResultWrapper<NothingResponse, ErrorResponse>
}