package com.horionDev.climbingapp.domain.model

import com.horionDev.climbingapp.data.model.dto.SectorDto
import kotlinx.serialization.Serializable

@Serializable
class CragDetailsDto(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val thumbnailUrl: String,
    val sectors: List<SectorDto>,
)

class UserProfile(
    val username: String,
    val email: String,
    val creationDate: String,
)