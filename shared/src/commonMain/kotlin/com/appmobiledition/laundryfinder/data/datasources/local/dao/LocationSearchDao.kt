package com.appmobiledition.laundryfinder.data.datasources.local.dao

import com.appmobiledition.laundryfinder.data.model.dto.LocationSearchDto
import com.appmobiledition.laundryfinder.data.model.dto.SearchDto

interface LocationSearchDao {
    suspend fun insertSearch(search: LocationSearchDto)
    suspend fun getAllSearchs(): List<LocationSearchDto>?
    suspend fun deleteSearchByLabel(label: String)
}