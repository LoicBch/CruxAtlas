package com.example.camperproglobal.domain.model.dao

import com.jetbrains.kmm.shared.domain.model.Spot

interface SpotDao {

    suspend fun insertSpot(spot: Spot)
    suspend fun getSpotByid(id: Long): Spot?
    suspend fun getAllSpots(): List<Spot>
    suspend fun deleteSpotById(id: Long)

}