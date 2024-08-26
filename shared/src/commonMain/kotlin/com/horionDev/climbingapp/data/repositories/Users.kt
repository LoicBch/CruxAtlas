package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.repositories.UserRepository

class Users(private var cruxAtlasApi: Api): UserRepository {
    override suspend fun login(authRequest: AuthRequest) = cruxAtlasApi.login(authRequest)
    override suspend fun authenticate(token: String) = cruxAtlasApi.authenticate(token)
    override suspend fun signup(
        username: String,
        password: String,
        email: String
    ) = cruxAtlasApi.signup(username, password, email)

}