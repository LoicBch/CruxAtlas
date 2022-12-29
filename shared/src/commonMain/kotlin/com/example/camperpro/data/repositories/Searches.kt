package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.local.dao.LocationSearchDao
import com.example.camperpro.data.datasources.local.dao.SearchDao
import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.repositories.SearchesRepository
import com.example.camperpro.utils.Constants
import toDto
import toVo

class Searches(
    private val searchDao: SearchDao,
    private val locationSearchDto: LocationSearchDao
) : SearchesRepository {

    override suspend fun delete(label: String) {
        searchDao.deleteSearchByLabel(label)
    }

    override suspend fun allOfCategory(category: String): List<Search>? {
        return when (category) {
            Constants.Persistence.SEARCH_CATEGORY_LOCATION -> locationSearchDto.getAllSearchs()
                ?.toVo()
            else -> searchDao.getAllSearchsOfCategory(category)?.toVo()
        }
    }

    override suspend fun add(search: Search) {
        searchDao.insertSearch(search.toDto())
    }
}