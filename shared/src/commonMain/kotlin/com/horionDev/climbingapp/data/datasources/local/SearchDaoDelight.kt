package com.horionDev.climbingapp.data.datasources.local

import com.horionDev.climbingapp.data.model.dto.SearchDto
import com.horionDev.climbingapp.database.Database
import com.horionDev.climbingapp.data.datasources.local.dao.SearchDao
import toDto

class SearchDaoDelight(db: Database) : SearchDao {

    private val queries = db.searchEntityQueries

    override suspend fun insertSearch(search: SearchDto) {
        queries.insertSearch(search.categoryKey, search.searchLabel, search.timeStamp)
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