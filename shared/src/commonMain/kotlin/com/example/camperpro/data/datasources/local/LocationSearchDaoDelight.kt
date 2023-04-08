package com.example.camperpro.data.datasources.local

import com.example.camperpro.data.datasources.local.dao.LocationSearchDao
import com.example.camperpro.data.model.dto.LocationSearchDto
import com.example.camperpro.database.CamperproDatabase
import toDto

class LocationSearchDaoDelight(db: CamperproDatabase) : LocationSearchDao {

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