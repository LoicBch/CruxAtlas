package com.horionDev.climbingapp.domain.repositories

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.model.composition.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.model.entities.User

interface UserRepository {
    suspend fun login(authRequest: AuthRequest): ResultWrapper<AuthResponse, ErrorResponse>
    suspend fun authenticate(token: String): ResultWrapper<User, ErrorResponse>
    suspend fun signup(username: String, password: String, email: String): ResultWrapper<AuthResponse, ErrorResponse>
}