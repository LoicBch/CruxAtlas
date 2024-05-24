package com.appmobiledition.laundryfinder.data.datasources.local

import com.appmobiledition.laundryfinder.data.datasources.local.dao.LocationSearchDao
import com.appmobiledition.laundryfinder.data.model.dto.LocationSearchDto
import com.appmobiledition.laundryfinder.database.CamperproDatabase
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