package com.example.camperproglobal.domain.model.dao

import com.example.camperpro.domain.model.Spot

interface SpotDao {

    suspend fun insertSpot(spot: Spot)
    suspend fun getSpotByid(id: Long): Spot?
    suspend fun getAllSpots(): List<Spot>
    suspend fun deleteSpotById(id: Long)

}