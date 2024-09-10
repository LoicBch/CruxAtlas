package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.data.model.responses.NothingResponse
import com.horionDev.climbingapp.domain.model.UserProfile
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.model.composition.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.User

interface UserRepository {
    suspend fun login(authRequest: AuthRequest): ResultWrapper<AuthResponse, ErrorResponse>
    suspend fun authenticate(token: String): ResultWrapper<User, ErrorResponse>
    suspend fun signup(username: String, password: String, email: String): ResultWrapper<String, ErrorResponse>
    suspend fun publicProfile(userId: Int): ResultWrapper<UserProfile, ErrorResponse>
    suspend fun forgotPassword(email: String): ResultWrapper<NothingResponse, ErrorResponse>
    suspend fun fetchFavorite(userId: Int): ResultWrapper<List<String>, ErrorResponse>
    suspend fun addCragAsFavoriteToUser(userId: Int, cragId: Int): ResultWrapper<List<String>, ErrorResponse>
    suspend fun removeCragAsFavoriteForUser(userId: Int, cragId: Int): ResultWrapper<List<String>, ErrorResponse>
}