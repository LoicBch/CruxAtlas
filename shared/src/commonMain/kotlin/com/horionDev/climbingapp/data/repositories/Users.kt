package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.repositories.UserRepository

class Users(private var cruxAtlasApi: Api) : UserRepository {
    override suspend fun login(authRequest: AuthRequest) = cruxAtlasApi.login(authRequest)
    override suspend fun authenticate(token: String) = cruxAtlasApi.authenticate(token)
    override suspend fun signup(
        username: String,
        password: String,
        email: String
    ) = cruxAtlasApi.signup(username, password, email)

    override suspend fun publicProfile(userId: Int) = cruxAtlasApi.getPublicProfile(userId)
    override suspend fun forgotPassword(email: String) = cruxAtlasApi.forgotPassword(email)
    override suspend fun fetchFavorite(userId: Int) = cruxAtlasApi.fetchFavorite(userId)

    override suspend fun addCragAsFavoriteToUser(
        userId: Int,
        cragId: Int
    ) = cruxAtlasApi.addCragAsFavoriteToUser(userId, cragId)

    override suspend fun removeCragAsFavoriteForUser(
        userId: Int,
        cragId: Int
    ) = cruxAtlasApi.removeCragAsFavoriteForUser(userId, cragId)
}