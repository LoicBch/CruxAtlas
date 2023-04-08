package com.example.camperpro.data.repositories

import com.example.camperpro.data.datasources.local.dao.FilterDao
import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.repositories.FilterRepository
import toDto
import toVo

class Filters(private val filterDao: FilterDao) : FilterRepository {
    override suspend fun delete(filter: Filter) {
        filterDao.deletefilter(filter.toDto())
    }

    override suspend fun allOfCategory(category: String): List<Filter> {
        return filterDao.getAllFiltersOfCategory(category)!!.toVo()
    }

    override suspend fun add(filter: Filter) {
        filterDao.insertfilter(filter.toDto())
    }

    override suspend fun deleteAllFilters() {
        filterDao.deleteAllFilters()
    }

    override suspend fun getFilterSaved(): List<Filter>? {
        return filterDao.getLastFiltersUsed()?.toVo()
    }

    override suspend fun resetActiveDealerFilter() {
        filterDao.resetActiveDealerFilter()
    }

    override suspend fun resetActiveEventFilter() {
        filterDao.resetActiveEventFilter()
    }
}