package com.appmobiledition.laundryfinder.domain.model

import io.ktor.http.*


data class Laundry(
    val id: Int,
    val name: String,
    val address: String,
    val phone: String,
    val email: String,
    val website: String,
    val latitude: Double,
    val longitude: Double,
    val images: List<ContentType.Image>
)