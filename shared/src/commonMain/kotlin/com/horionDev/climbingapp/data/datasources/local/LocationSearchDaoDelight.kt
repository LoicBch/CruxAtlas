package com.horionDev.climbingapp.data.datasources.local

import com.horionDev.climbingapp.data.datasources.local.dao.LocationSearchDao
import com.horionDev.climbingapp.data.model.dto.LocationSearchDto
import com.horionDev.climbingapp.database.Database
import toDto

class LocationSearchDaoDelight(db: Database) : LocationSearchDao {

    private val queries = db.locationSearchEntityQueries

    override suspend fun insertSearch(search: LocationSearchDto) {
        queries.insertSearch(search.label, search.timeStamp, search.lat, search.lon)
    }

    override suspend fun getAllSearchs(): List<LocationSearchDto> {
        return queries.getAllSearchs()
            .executeAsList()
            .toDto()
    }

    override suspend fun deleteSearchByLabel(label: String) {
        queries.deleteSearchBySearchLabel(label)
    }
}