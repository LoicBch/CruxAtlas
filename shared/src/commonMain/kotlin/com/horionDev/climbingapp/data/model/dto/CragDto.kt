package com.horionDev.climbingapp.data.model.dto

import com.horionDev.climbingapp.domain.model.composition.BoundingBox
import com.horionDev.climbingapp.domain.model.entities.Sector
import kotlinx.serialization.Serializable

@Serializable
class CragDto(
    val id: Int,
    val areaId: Int,
    val name: String,
    val description: String,
    val sectors: List<Sector>,
    val latitude: Double,
    val longitude: Double,
    val thumbnailImageUrl: String,
    val boundingBox: BoundingBox = BoundingBox(0.0, 0.0, 0.0, 0.0),
    val orientation: String,
    val altitude: String,
    val approachLenght: String
)

@Serializable
data class UserProfileDto(
    val username: String,
    val email: String,
    val creationDate: String,
)

@Serializable
data class NewsItemDto(
    val id : Int,
    val title: String,
    val description: String,
    val imageUrl: String,
//    val date: String,
)

