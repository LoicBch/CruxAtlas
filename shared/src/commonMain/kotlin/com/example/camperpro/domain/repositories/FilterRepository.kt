package com.example.camperpro.domain.repositories

import com.example.camperpro.domain.model.composition.Filter

interface FilterRepository {
    suspend fun delete(filter: Filter)
    suspend fun allOfCategory(category: String): List<Filter>?
    suspend fun add(filter: Filter)
    suspend fun deleteAllFilters()
    suspend fun getFilterSaved(): List<Filter>?
    suspend fun resetActiveDealerFilter()
    suspend fun resetActiveEventFilter()
}