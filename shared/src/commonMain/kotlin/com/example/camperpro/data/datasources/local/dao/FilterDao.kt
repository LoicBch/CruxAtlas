package com.example.camperpro.data.datasources.local.dao

import com.example.camperpro.data.model.dto.FilterDto

interface FilterDao {
    suspend fun insertfilter(filter: FilterDto)
    suspend fun getAllFiltersOfCategory(categoryKey: String): List<FilterDto>?
    suspend fun deletefilter(filter: FilterDto)
    suspend fun deleteAllFilters()
    suspend fun getSelectedFilter(): FilterDto?
    suspend fun getLastFiltersUsed(): List<FilterDto>?
    suspend fun resetActiveDealerFilter()
    suspend fun resetActiveEventFilter()
}