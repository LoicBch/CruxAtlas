package com.example.camperpro.data.datasources.local.dao

import com.example.camperpro.data.model.dto.SearchDto

interface SearchDao {

    suspend fun insertSearch(search : SearchDto)
    suspend fun getAllSearchOfCategory(categoryKey: String): List<SearchDto>?
    suspend fun deleteSearchByLabel(label: String)

}