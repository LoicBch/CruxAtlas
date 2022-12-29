package com.example.camperpro.data.datasources.local

import com.example.camperpro.data.model.dto.SearchDto
import com.example.camperpro.database.CamperproDatabase
import com.example.camperpro.data.datasources.local.dao.SearchDao
import toDto

class SearchDaoDelight(db: CamperproDatabase) : SearchDao {

    private val queries = db.searchEntityQueries

    override suspend fun insertSearch(search: SearchDto) {
        queries.insertSearch(0, search.categoryKey, search.searchLabel, search.timeStamp)
    }

    override suspend fun getAllSearchsOfCategory(categoryKey: String): List<SearchDto> {
        return queries.getAllSearchsOfCategory(searchCategoryKey = categoryKey)
            .executeAsList()
            .toDto()
    }

    override suspend fun deleteSearchByLabel(label: String) {
        queries.deleteSearchBySearchLabel(label)
    }
}