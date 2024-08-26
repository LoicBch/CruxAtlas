package com.horionDev.climbingapp.domain.usecases

import com.horionDev.climbingapp.data.ResultWrapper
import com.horionDev.climbingapp.domain.model.composition.AuthRequest
import com.horionDev.climbingapp.domain.model.composition.AuthResponse
import com.horionDev.climbingapp.domain.model.composition.ErrorResponse
import com.horionDev.climbingapp.domain.repositories.UserRepository
import com.horionDev.climbingapp.utils.Constants
import com.horionDev.climbingapp.utils.KMMPreference
import com.horionDev.climbingapp.utils.SessionManager.user
import toVo

class LoginUseCase(
    private val users: UserRepository,
    private val kmmPreference: KMMPreference,
) {
    suspend operator fun invoke(authRequest: AuthRequest): ResultWrapper<AuthResponse, ErrorResponse> {
        return try {
            val authResponse = users.login(authRequest)
            if (authResponse is ResultWrapper.Success) {
                user = authResponse.value.user.toVo()
//                val favorites = users.fetchFavorite(user.id)
//                val visited = users.fetchVisited(user.id)
//                if (favorites is ResultWrapper.Success) {
//                    user.favorites = favorites.value.toMutableList()
//                }
//                if (visited is ResultWrapper.Success) {
//                    user.visited = visited.value.toMutableList()
//                }
//                kmmPreference.put(Constants.Preferences.SESSION_TOKEN, authResponse.value.token)
            }
            return authResponse
        } catch (throwable: Throwable) {
            ResultWrapper.Failure.UnknownError(throwable.toString())
        }
    }
}