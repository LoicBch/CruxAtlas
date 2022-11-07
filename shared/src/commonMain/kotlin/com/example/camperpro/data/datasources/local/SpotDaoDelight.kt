package com.example.camperproglobal.data.datasources.local

import com.example.camperproglobal.domain.model.dao.SpotDao
import com.jetbrains.kmm.shared.domain.model.Spot
import com.example.camperpro.database.CamperproDatabase

class SpotDaoDelight(db: CamperproDatabase) : SpotDao {

    private val queries = db.spotQueries

    override suspend fun insertSpot(spot: Spot) {
        queries.insertSpot(14, spot.name, spot.name)
    }

    override suspend fun getSpotByid(id: Long): Spot? {
//        mapper
//        return queries.getSpotById(14).executeAsOneOrNull(). mapper
        TODO("Not yet implemented")
    }

    override suspend fun getAllSpots(): List<Spot> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpotById(id: Long) {
        TODO("Not yet implemented")
    }
}