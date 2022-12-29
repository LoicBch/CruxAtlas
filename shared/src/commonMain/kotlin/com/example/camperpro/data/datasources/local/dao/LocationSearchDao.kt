package com.example.camperpro.data.datasources.local.dao

import com.example.camperpro.data.model.dto.LocationSearchDto
import com.example.camperpro.data.model.dto.SearchDto

interface LocationSearchDao {
    suspend fun insertSearch(search: LocationSearchDto)
    suspend fun getAllSearchs(): List<LocationSearchDto>?
    suspend fun deleteSearchByLabel(label: String)
}