package com.horionDev.climbingapp.domain.model.entities

import com.horionDev.climbingapp.utils.CommonParcelable
import com.horionDev.climbingapp.utils.CommonParcelize

@CommonParcelize
data class User(
    val id: Int = -1,
    val username: String = "guest",
    val password: String = "",
    var email: String = "",
    var country: String = "",
    var city: String = "",
    var gender: String = "",
    var age: Int = 0,
    var weight: Int = 0,
    var height: Int = 0,
    var climbingSince: String = "",
    var statut: String = "",
//    Remove this and check on server if user is allowed to access functionalities
    var isSubscribe: Boolean = false,
    var favorites: MutableList<String> = mutableListOf(),
    var imageUrl: String? = null
) : CommonParcelable