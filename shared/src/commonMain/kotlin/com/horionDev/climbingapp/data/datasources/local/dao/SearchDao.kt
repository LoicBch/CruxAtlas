package com.horionDev.climbingapp.data.datasources.local.dao

import com.horionDev.climbingapp.data.model.dto.SearchDto

interface SearchDao {
    suspend fun insertSearch(search : SearchDto)
    suspend fun getAllSearchsOfCategory(categoryKey: String): List<SearchDto>?
    suspend fun deleteSearchByLabel(label: String)
}