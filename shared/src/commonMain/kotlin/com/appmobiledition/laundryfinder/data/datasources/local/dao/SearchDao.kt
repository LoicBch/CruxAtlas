package com.appmobiledition.laundryfinder.data.datasources.local.dao

import com.appmobiledition.laundryfinder.data.model.dto.SearchDto

interface SearchDao {
    suspend fun insertSearch(search : SearchDto)
    suspend fun getAllSearchsOfCategory(categoryKey: String): List<SearchDto>?
    suspend fun deleteSearchByLabel(label: String)
}