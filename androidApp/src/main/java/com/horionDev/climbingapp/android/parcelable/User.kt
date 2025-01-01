package com.horionDev.climbingapp.android.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserParcel(
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
    var isSubscribe: Boolean = false,
    var favorites: MutableList<String> = mutableListOf(),
    var imageUrl: String? = null
): Parcelable