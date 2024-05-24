package com.appmobiledition.laundryfinder.data.datasources.local

import com.appmobiledition.laundryfinder.data.model.dto.SearchDto
import com.appmobiledition.laundryfinder.database.CamperproDatabase
import com.appmobiledition.laundryfinder.data.datasources.local.dao.SearchDao
import toDto

class SearchDaoDelight(db: CamperproDatabase) : SearchDao {

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