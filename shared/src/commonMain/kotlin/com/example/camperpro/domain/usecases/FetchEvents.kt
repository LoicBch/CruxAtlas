package com.example.camperpro.domain.usecases

import com.example.camperpro.data.ResultWrapper
import com.example.camperpro.domain.model.Event
import com.example.camperpro.domain.repositories.EventRepository
import com.example.camperpro.domain.repositories.FilterRepository
import com.example.camperpro.utils.FilterType

class FetchEvents(
    private val events: EventRepository,
    private val filters: FilterRepository
) : IBaseUsecase {
    suspend operator fun invoke(): ResultWrapper<List<Event>> {

        val countryFilters =
            filters.getFilterSaved()
                ?.filter { it.category == FilterType.COUNTRIES && it.isSelected }
                ?.map { it.filterId }

        return events.all(countryFilters!!.first())
    }
}