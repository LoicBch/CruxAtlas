package com.horionDev.climbingapp.data.repositories

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.data.model.dto.AuthRequest
import com.horionDev.climbingapp.data.model.dto.BoulderLogDto
import com.horionDev.climbingapp.data.model.dto.RouteLogDto
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

    override suspend fun updatePhoto(
        userId: Int,
        byteArray: ByteArray
    ) = cruxAtlasApi.updatePhoto(userId, byteArray)


    override suspend fun fetchRouteLogs(userId: Int) = cruxAtlasApi.fetchRouteLogs(userId)

    override suspend fun fetchBoulderLogs(userId: Int) = cruxAtlasApi.fetchBoulderLogs(userId)
    override suspend fun updateUser(userDto: UserDto) = cruxAtlasApi.updateUser(userDto)
    override suspend fun addRouteLog(
        routeLog: RouteLogDto,
        userId: Int,
        routeId: Int
    ) = cruxAtlasApi.addRouteLog(userId, routeId, routeLog)

    override suspend fun addBoulderLog(
        boulderLog: BoulderLogDto,
        userId: Int,
        boulderId: Int
    ) = cruxAtlasApi.addBoulderLog(userId, boulderId, boulderLog)
}