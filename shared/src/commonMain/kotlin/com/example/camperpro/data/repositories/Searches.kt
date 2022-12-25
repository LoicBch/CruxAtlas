package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.local.dao.SearchDao
import com.example.camperpro.domain.model.Search
import com.example.camperpro.domain.repositories.SearchesRepository
import toDto
import toVo

class Searches(private val searchDao: SearchDao) : SearchesRepository {

    override suspend fun delete(label: String) {
        searchDao.deleteSearchByLabel(label)
    }

    override suspend fun allOfCategory(category: String): List<Search>? {
        return searchDao.getAllSearchOfCategory(category)?.toVo()
    }

    override suspend fun add(search: Search) {
        searchDao.insertSearch(search.toDto())
    }

}