package com.horionDev.climbingapp.utils

import com.horionDev.climbingapp.domain.model.entities.User


object SessionManager {

    var user: User = User()

    fun isLogged() = user.username != "guest"

    fun logout(kmmPreference: KMMPreference) {
        kmmPreference.remove("user_id")
        kmmPreference.remove("session_token")
        user = User()
    }

    fun isSubscribed(): Boolean {
        return (isLogged() && user.isSubscribe)
    }
}