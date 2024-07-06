package com.horionDev.climbingapp.data.datasources.local.dao

import com.horionDev.climbingapp.data.model.dto.LocationSearchDto

interface LocationSearchDao {
    suspend fun insertSearch(search: LocationSearchDto)
    suspend fun getAllSearchs(): List<LocationSearchDto>?
    suspend fun deleteSearchByLabel(label: String)
}