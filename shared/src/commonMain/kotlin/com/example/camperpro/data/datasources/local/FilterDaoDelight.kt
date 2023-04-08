package com.example.camperpro.data.datasources.local

import com.example.camperpro.data.datasources.local.dao.FilterDao
import com.example.camperpro.data.model.dto.FilterDto
import com.example.camperpro.database.CamperproDatabase
import com.example.camperpro.utils.FilterType
import toDto

class FilterDaoDelight(db: CamperproDatabase) : FilterDao {

    private val queries = db.filterEntityQueries

    override suspend fun insertfilter(filter: FilterDto) {
        if (filter.category == FilterType.COUNTRIES.name) {
            queries.resetSelectedEventFilters()
        } else {
            queries.resetSelectedDealerFilters()
        }
        queries.insertFilter(filter.category, filter.filterId, filter.isSelected)
    }

    override suspend fun getAllFiltersOfCategory(categoryKey: String): List<FilterDto> {
        return queries.getAllFilterForCategory(categoryKey).executeAsList().toDto()
    }

    override suspend fun deletefilter(filter: FilterDto) {
        queries.deleteFilter(filter.category, filter.filterId)
    }

    override suspend fun deleteAllFilters() {
        queries.deleteAllFilter()
    }

    override suspend fun getSelectedFilter(): FilterDto? {
        return queries.getSelectedFilter().executeAsOneOrNull()?.toDto()
    }

    override suspend fun getLastFiltersUsed(): List<FilterDto>? {
        val filters = queries.getAllFilters().executeAsList().map { it.toDto() }
        return filters.ifEmpty { null }
    }

    override suspend fun resetActiveDealerFilter() {
        queries.resetSelectedDealerFilters()
    }

    override suspend fun resetActiveEventFilter() {
        queries.resetSelectedEventFilters()
    }
}